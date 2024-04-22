package ro.tuiasi.student.carla.proiect.services

import org.springframework.http.HttpStatus
import ro.tuiasi.student.carla.proiect.services.interfaces.IVacationPlannerService
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.ItineraryPoi
import ro.tuiasi.student.carla.proiect.gateways.places.PlacesApiGateway
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails
import ro.tuiasi.student.carla.proiect.gateways.webScraping.WebScrapingApiGateway
import ro.tuiasi.student.carla.proiect.models.Poi
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput

@Service
class VacationPlannerService(
    private val chatGptService: ChatGptService,
    private val imageGeneratorService: ImageGeneratorService,
    private val customSearchService: CustomSearchService,
    private val placesApiGateway: PlacesApiGateway,
    private val webScrapingApiGateway: WebScrapingApiGateway
) : IVacationPlannerService {
    override fun vacationPlanner(vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
        // search on Google for the given destination
        val searchPrompt = customSearchService.generatePromptCustomSearch(vacationPlannerInput)
        val searchResults: List<SearchDetails> = customSearchService.search(searchPrompt)

        // empty list means that we have a city as destination, and we will use it for chatgpt
        val destination: String = if (searchResults.isEmpty()) {
            vacationPlannerInput.destination
        }
        // not empty list means that we have a continent, a country or nothing as destination
        else {
            val cities = mutableMapOf<String, Int>()

            // for 2 random results, call web scraping and get the content
            val randomResults = searchResults.shuffled().take(2)
            for (element in randomResults) {
                val content = webScrapingApiGateway.getWebScrapingResults(element.link) ?: ""

                // for every content, call chatgpt and get the cities
                if (content != "") {
                    val citiesFromContent =
                        chatGptService.extractCitiesFromText(content, vacationPlannerInput.destination)

                    for (city in citiesFromContent) {
                        cities[city] = cities.getOrDefault(city, 0) + 1
                    }
                }
            }

            if (cities.isEmpty()) {
                throw HttpClientErrorException(HttpStatus.NO_CONTENT, "No cities founded for the given destination.")
            }

            // remove from the list that one that is the same with the starting point
            // (just for the case when the destination is a continent or a country)
            // get the best city from the list of cities
            // which means the city with the most frequency in the list
            // if there are more cities with the same frequency, we will choose it randomly
            if (vacationPlannerInput.startingPoint != vacationPlannerInput.destination) {
                cities.remove(vacationPlannerInput.startingPoint.split(",")[0])
            }
            val maxFrequency = cities.values.max()
            val mostFrequentCities = cities.filterValues { it == maxFrequency }.keys.toList()
            mostFrequentCities.shuffled().first()
        }

        // generate pois with chatgpt
        val chatGptOutput = chatGptService.generatePoi(
            city = destination,
            gender = vacationPlannerInput.gender,
            attendant = vacationPlannerInput.attendant,
            season = vacationPlannerInput.season,
            transport = vacationPlannerInput.transport,
            interests = vacationPlannerInput.interests,
            otherInterests = vacationPlannerInput.otherInterests
        ) ?: throw HttpClientErrorException(
            HttpStatus.NO_CONTENT,
            "ChatGpt can't generate pois for the given destination: $destination"
        )

        // call places api for every poi
        val listOfPois: MutableList<Poi> = buildListOfPois(chatGptOutput.points_of_interest, destination)

        // call OpenAiApi for the photo
        val photoOfItinerary = imageGeneratorService.generateImage(destination)

        val vacationPlannerOutput = VacationPlannerOutput(
            photo = photoOfItinerary,
            name = chatGptOutput.tour_name,
            destination = destination,
            startingPoint = vacationPlannerInput.startingPoint,
            season = vacationPlannerInput.season,
            attendant = vacationPlannerInput.attendant,
            budget = vacationPlannerInput.budget,
            transport = vacationPlannerInput.transport,
            distance = 0.0,
            poisNumber = listOfPois.size,
            highlights = chatGptOutput.highlights,
            pois = listOfPois
        )
        if (vacationPlannerOutput.pois.isEmpty() || vacationPlannerOutput.destination == "") {
            throw HttpClientErrorException(HttpStatus.NO_CONTENT, "No tour founded for the given destination.")
        }

        // return vacation planner output
        return vacationPlannerOutput
    }

    private fun buildListOfPois(pointsOfInterest: List<ItineraryPoi>, destination: String): MutableList<Poi> {
        val pois = mutableListOf<Poi>()

        pointsOfInterest.forEach {
            val placeDetails = placesApiGateway.searchPlace(it.name + ", " + destination)
            if (placeDetails != null) {
                pois.add(
                    Poi(
                        name = placeDetails.placeName,
                        photo = placeDetails.photoReference,
                        description = it.description,
                        tags = it.tags,
                        stars = placeDetails.rating,
                        address = placeDetails.placeAddress,
                        phone = placeDetails.phone,
                        website = placeDetails.website,
                        schedule = placeDetails.schedule,
                        latitude = placeDetails.latitude,
                        longitude = placeDetails.longitude
                    )
                )
            }
        }

        if (pois.isEmpty()) {
            throw HttpClientErrorException(HttpStatus.NO_CONTENT, "No pois founded for the given destination.")
        }

        return pois
    }
}
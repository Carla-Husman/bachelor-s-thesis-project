package ro.tuiasi.student.carla.proiect.services

import ro.tuiasi.student.carla.proiect.services.interfaces.IVacationPlannerService
import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.ItineraryPoi
import ro.tuiasi.student.carla.proiect.gateways.places.PlacesApiGateway
import ro.tuiasi.student.carla.proiect.gateways.search.SearchApiGateway
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails
import ro.tuiasi.student.carla.proiect.models.Poi
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput

@Service
class VacationPlannerService (
    private val chatGptService: ChatGptService,
    private val searchApiGateway: SearchApiGateway,
    private val placesApiGateway: PlacesApiGateway,
) : IVacationPlannerService {
    override fun vacationPlanner(vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
        // build the prompt for Google search
        val searchPrompt = generatePromptForGoogleSearch(vacationPlannerInput)

        var inputForChatGpt = ""

        if (searchPrompt != ""){
            // search on Google
            val searchResults : List<SearchDetails> = searchApiGateway.search(searchPrompt)

            // get content from pages
            inputForChatGpt = "Iași, Romania"
        }

        // generate pois with chatgpt
        val chatGptOutput = chatGptService.generatePoi(
            city = inputForChatGpt,
            transport = vacationPlannerInput.transport.toString().lowercase(),
            interests = vacationPlannerInput.interests
        ) ?: throw Exception("ChatGpt output is null.")

        // call places api for every poi
        val listOfPois : MutableList<Poi> = buildListOfPois(chatGptOutput.points_of_interest)

        // return vacation planner output
        return VacationPlannerOutput(
            photo = "photo",
            name = chatGptOutput.tour_name,
            destination = inputForChatGpt,
            season = vacationPlannerInput.season,
            attendant = vacationPlannerInput.attendant,
            distance = 0.0,
            poisNumber = listOfPois.size,
            highlights = chatGptOutput.highlights,
            pois = listOfPois
        )
    }

    private fun generatePromptForGoogleSearch(vacationPlannerInput: VacationPlannerInput): String {

        // if destination is null, we will search for the best trip in the world
        if (vacationPlannerInput.destination == null) {
            return "Best" + (vacationPlannerInput.budget?.let { " $it" } ?: "") + "trip destination in whole the world" +
                    (vacationPlannerInput.attendant?.let { " for $it" } ?: "") +
                    (vacationPlannerInput.season?.let { ", $it" } ?: "") +
                    "intext:(${vacationPlannerInput.interests.joinToString(" OR ")})"
        }
        // if destination is a continent or a country
        else if (vacationPlannerInput.destination.matches(Regex("^(Asia|Africa|North America|South America|Antarctica|Europe|Australia)\$")) ||
            !vacationPlannerInput.destination.matches(Regex("^(([a-zA-Z\\u0080-\\u024F ]|(?:[-'’.`]))*, ([a-zA-Z\\u0080-\\u024F ]|(?:[-'’.`]))*)\$"))){
            return "Best" + (vacationPlannerInput.budget?.let { " $it" } ?: "") + "trip destination in ${vacationPlannerInput.destination}" +
                    (vacationPlannerInput.attendant?.let { " for $it" } ?: "") +
                    (vacationPlannerInput.season?.let { ", $it" } ?: "") +
                    "intext:(${vacationPlannerInput.interests.joinToString(" OR ")})"
        }

        // destination is a city
        return ""
    }

    private fun buildListOfPois(pointsOfInterest: List<ItineraryPoi>): MutableList<Poi>{
        val pois = mutableListOf<Poi>()

        pointsOfInterest.forEach {
            val placeDetails = placesApiGateway.searchPlace(it.name)
            if (placeDetails != null) {
                pois.add(
                    Poi(
                        name = placeDetails.placeName,
                        photo = "",
                        description = it.description,
                        tags = it.tags,
                        stars = placeDetails.rating,
                        address = placeDetails.placeAddress,
                        phone = placeDetails.phone ?: "",
                        website = placeDetails.website ?: "",
                        schedule = placeDetails.schedule,
                        latitude = placeDetails.latitude,
                        longitude = placeDetails.longitude
                    )
                )
            }
        }

        return pois
    }
}
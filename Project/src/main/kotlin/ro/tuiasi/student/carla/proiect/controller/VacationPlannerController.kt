package ro.tuiasi.student.carla.proiect.controller

import ro.tuiasi.student.carla.proiect.services.VacationPlannerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.Itinerary
import ro.tuiasi.student.carla.proiect.gateways.places.PlacesApiGateway
import ro.tuiasi.student.carla.proiect.gateways.places.dto.PlaceDetails
import ro.tuiasi.student.carla.proiect.gateways.search.SearchApiGateway
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails
import ro.tuiasi.student.carla.proiect.gateways.webScraping.WebScrapingApiGateway
import ro.tuiasi.student.carla.proiect.models.Poi
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput
import ro.tuiasi.student.carla.proiect.models.utils.*
import ro.tuiasi.student.carla.proiect.services.ChatGptService
import ro.tuiasi.student.carla.proiect.services.FilterCitiesService

@RestController
@RequestMapping("/api/v1/test")
@Tag(
    name = "Vacation Planner Controller", description = "This provides all operations for managing vacation planner"
)
@CrossOrigin(origins = ["http://localhost:4200"])
class VacationPlannerController(
    private val vacationPlannerService: VacationPlannerService,
    private val chatGptService: ChatGptService,
    private val placesApiGateway: PlacesApiGateway,
    private val searchGateway: SearchApiGateway,
    private val webScrapingGateway: WebScrapingApiGateway,
    private val citiesService: FilterCitiesService
) {
    @Operation(
        summary = "Vacation Planner",
        description = "This operation returns a personalized vacation plan based on the provided input parameters. "
    )
    @PostMapping("/planner")
    fun vacationPlanner(@RequestBody vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
        /*
        {
            "destination": "Iasi, Romania",
            "startingPoint": "Bucuresti",
            "age": 25,
            "gender": "FEMALE",
            "attendant": "Family",
            "season": "SUMMER",
            "transport": "CAR",
            "budget": "MIDRANGE",
            "interests": ["MUSEUM", "FOOD", "BOOKS"],
            "otherInterests": "Some other interests"
        }
         */
        return vacationPlannerService.vacationPlanner(vacationPlannerInput)
    }

    @Operation(
        summary = "Generate a Point of Interest (POI)",
        description = "Used for testing the ChatGPT service to generate a Point of Interest (POI). " +
                "The operation showcases the ability to generate content related to a specific point of interest."
    )
    @GetMapping("/chatgpt-poi")
    fun chatGpt(
        @RequestParam(required = true) destination: String,
        @RequestParam(required = false) gender: Gender?,
        @RequestParam(required = false) attendant: Attendants?,
        @RequestParam(required = false) season: Season?,
        @RequestParam(required = false) transport: Transport?,
        @RequestParam(required = true) interests: List<Interests>,
        @RequestParam(required = false) otherInterests: String?
    ): Itinerary? {
        return chatGptService.generatePoi(
            city = destination,
            gender = gender,
            attendant = attendant,
            season = season,
            transport = transport,
            interests = interests,
            otherInterests = otherInterests
        )
    }

    @Operation(
        summary = "Extract Cities from HTML",
        description = "Used for testing the ChatGPT service to extract cities from HTML content. "
    )
    @GetMapping("/chatgpt-cities")
    fun chatGptCities(): List<String>? {
        return chatGptService.extractCitiesFromText(
            "Best Romanian fall destinations - RomaniaTourStore\n" +
                    "+40 (0) 758 990 033\n" +
                    "book@romaniatourstore.com\n" +
                    "To Search, type and hit enter\n" +
                    "0\n" +
                    "Best Romanian fall destinations\n" +
                    "2 September 2016&nbsp;\n" +
                    "Do you feel the need of a getaway, even for just a weekend? Why not take a trip to Romania and see the way the nature is changing colors into the new season. You can opt for an urban adventure in one of Romania’s cities, learn more about the rural life in some of Why should you choose Romania as a travel fall destination? Because it’s one of the most affordable ones in Europe, it offers a lot of variety in terms of travel destinations and tourist attractions and it’s relatively accessible. Here are some great Romanian travel destinations for the fall season:\n" +
                    "Maramures region\n" +
                    "The Maramures region is a great option for any fall getaway. The villages scattered on hills that change color in rust hues are a perfect place to relax. You can do hiking, visits to the Merry Cemetery in Sapanta, unique in the world, and to the Barsana and Ieud monasteries. If you decide to go hiking, don’t forget to pack some warm clothes with you and a rain coat.\n" +
                    "Sighisoara\n" +
                    "If you’re not interested in spending some time in nature, but rather go on a romantic getaway, you can opt for a If you take a trip to Sighisoara at the end of October, when Halloween is just around the corner, numerous events and parties are organized in various hot sports of the town to celebrate “the day of the undead.” After all, Vlad Tepes’ home is also located in Sighisoara and you can visit it, as it functions as a thematic restaurant and museum at the same time.\n" +
                    "Magura village\n" +
                    "Located between Bucegi and Piatra Craiului Mountains, at an altitude of approximatley 1,000 meters, the village of Magura offers breathtaking scenery, especially during the fall season when, nature overwhelms you with all its colors, giving you a nice feeling of tranquility.\n" +
                    "What can you do in Magura? Of course, there are a lot of trails you can cover on foot or by bike. The simplest and most satisfying one is a trail to Curmatura and another simple trail, even for beginner is the one to the Zarnesti Steeps.\n" +
                    "Brasov city\n" +
                    "As we mentioned before, You can’t pass through Brasov without passing through Sfatului Square (Piata Sfatului). During medieval times, this was the place where Romanian, Saxon and Hungarian merchants used to organize fairs. In the building with an observation tower, also known as Casa Sfatului, there were arranged chamber for magistrates. In time, Casa Sfatului was a city hall and now it functions as a History Museum.\n" +
                    "Of course, near the square is the famous Black Church, the largest gothic cathedral from southeastern Europe. The church was built at the end of the fourteenth century as a Catholic church, but after the religious reforms established by Johannes Honterus in Transylvania, it was converted into an Evangelical church.\n" +
                    "&nbsp;\n" +
                    "The medieval sites also include two remaining gates, Ecaterina Gate and Schei Gate, as well as various medieval towers dedicated to the various guilds.\n" +
                    "Omu Peak in the Bucegi Mountains\n" +
                    "Reaching the Omu Peak is always interesting, because you have to pass by the Babele and the Sphinx, the mysterious and unique natural rock formations found in the Bucegi Mountains. Some say aliens made them, but that’s a story for another time. After that, every hiker going up on the Bucegi will inevitably rest a little or spend the night at the Omu Hut. The trail offers some breathtaking, picture – perfect landscapes. Also, you shouldn’t be surprised if you encounter some shepherds with their sheep and dogs just casually lounging in the grass.\n" +
                    "Two castles: Bran and Peles\n" +
                    "Prahova Valley is not only a crowded during winter, when tourists enjoy skiing or practicing other types of winter sports in Sinaia, Busteni, Predeal or Poiana Brasov. This is also a great fall destination if you want to visit some of the most beautiful castles in Europe: Bran and Peles.\n" +
                    "Bran Castle is located near Brasov and it’s now practically an amusement park for Dracula’s fans. However, if you’re not into the whole vampire myth thing, you can still visit the rooms of the castle, admire the unique architectural style and various items dating back to centuries ago.\n" +
                    "You can visit both castles in just one day, by booking our If you’re not convinced yet that Romanian country is a great option for fall destinations, be it weekend getaways or longer trips, take a look at our other\n" +
                    "-->",
            "Romania"
        )
    }

    @Operation(
        summary = "Get Place Information",
        description = "Retrieves information about a specific place of interest (POI) for testing purposes. " +
                "The provided destination parameter specifies the place for which information is requested."
    )
    @GetMapping("/places", params = ["destination"])
    fun places(@RequestParam(required = true) destination: String): PlaceDetails? {
        // Botanical Garden, Iasi, Romania
        return placesApiGateway.searchPlace(destination)
    }

    @Operation(
        summary = "Custom Search",
        description = "Performs a custom search for tourist destinations based on the provided prompt. " +
                "The prompt is a search query that may include keywords related to travel destinations and user interests"
    )
    @GetMapping("/custom-search", params = ["prompt"])
    fun customSearch(@RequestParam(required = true) prompt: String): List<SearchDetails> {
        // "Best Cheap Romania travel destinations Autumn intext:(art OR museum OR history)"
        return searchGateway.search(prompt)
    }

    @Operation(
        summary = "Web Scraping with Content Cleanup",
        description = "Retrieves information by performing web scraping on the provided URL. " +
                "The operation includes cleaning up the content by removing JavaScript, HTML and CSS code. "
    )
    @GetMapping("/web-scraping", params = ["url"])
    fun webScraping(@RequestParam(required = true) url: String): String? {
        // https://romaniatourstore.com/blog/best-romanian-fall-destinations/
        return webScrapingGateway.getWebScrapingResults(url)
    }

    @GetMapping("/resource/{id}")
    fun getResource(@PathVariable id: Long): String {
        // Simulate a resource not found exception
        throw HttpClientErrorException(HttpStatus.NO_CONTENT, "Resource not found")
    }

    @PostMapping("/temp")
    fun get(@RequestBody vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
        Thread.sleep(6000)

        return VacationPlannerOutput(
            photo = "photo",
            name = "California Tour",
            destination = "California",
            season = null,
            attendant = null,
            distance = 0.0,
            poisNumber = 5,
            highlights = listOf(
                "Explore the vibrant art scene in California",
                "Immerse yourself in the breathtaking natural landscapes",
                "Embark on exhilarating hiking adventures"
            ),
            pois = listOf(
                Poi(
                    name = "Yosemite National Park",
                    photo = "",
                    description = "Yosemite National Park is renowned for its granite cliffs, waterfalls, clear streams, and giant sequoias. It offers a perfect blend of nature and hiking opportunities.",
                    longitude = -119.5383294,
                    latitude = 37.8651011,
                    tags = listOf("nature", "hiking"),
                    stars = (4.8).toFloat(),
                    address = "California, United States",
                    phone = "+1 209-372-0200",
                    website = "https://www.nps.gov/yose/index.htm",
                    schedule = listOf(
                        "Monday: Open 24 hours",
                        "Tuesday: Open 24 hours",
                        "Wednesday: Open 24 hours",
                        "Thursday: Open 24 hours",
                        "Friday: Open 24 hours",
                        "Saturday: Open 24 hours",
                        "Sunday: Open 24 hours"
                    )
                ),
                Poi(
                    name = "The Getty",
                    photo = "",
                    description = "The Getty Center is a renowned art museum and architectural masterpiece, featuring an impressive collection of European paintings, sculptures, and decorative arts.",
                    longitude = -118.4740954,
                    latitude = 34.07803579999999,
                    tags = listOf("art", "nature"),
                    stars = (4.8).toFloat(),
                    address = "1200 Getty Center Dr, Los Angeles, CA 90049, United States",
                    phone = "+1 310-440-7300",
                    website = "https://www.getty.edu/visit/center/",
                    schedule = listOf(
                        "Monday: Closed",
                        "Tuesday: 10:00\u202fAM – 5:30\u202fPM",
                        "Wednesday: 10:00\u202fAM – 5:30\u202fPM",
                        "Thursday: 10:00\u202fAM – 5:30\u202fPM",
                        "Friday: 10:00\u202fAM – 5:30\u202fPM",
                        "Saturday: 10:00\u202fAM – 8:00\u202fPM",
                        "Sunday: 10:00\u202fAM – 5:30\u202fPM"
                    )
                ),
                // Adaugă celelalte obiecte Poi
                Poi(
                    name = "Big Sur",
                    photo = "",
                    description = "Big Sur is a rugged stretch of California's central coast, known for its dramatic cliffs, stunning ocean views, and numerous hiking trails that lead to hidden coves and waterfalls.",
                    longitude = -121.8080556,
                    latitude = 36.2704233,
                    tags = listOf("nature", "hiking"),
                    stars = (-1.0).toFloat(),
                    address = "Big Sur, CA, USA",
                    phone = "",
                    website = "",
                    schedule = List(0) { "" }
                ),
                Poi(
                    name = "The Broad",
                    photo = "",
                    description = "The Broad is a contemporary art museum in downtown Los Angeles, showcasing an extensive collection of modern and contemporary art, including works by renowned artists.",
                    longitude = -118.2501802,
                    latitude = 34.0545021,
                    tags = listOf("art"),
                    stars = (4.7).toFloat(),
                    address = "221 S Grand Ave, Los Angeles, CA 90012, United States",
                    phone = "+1 213-232-6200",
                    website = "https://www.thebroad.org/",
                    schedule = listOf(
                        "Monday: Closed",
                        "Tuesday: 11:00\u202fAM – 5:00\u202fPM",
                        "Wednesday: 11:00\u202fAM – 5:00\u202fPM",
                        "Thursday: 11:00\u202fAM – 8:00\u202fPM",
                        "Friday: 11:00\u202fAM – 5:00\u202fPM",
                        "Saturday: 10:00\u202fAM – 6:00\u202fPM",
                        "Sunday: 10:00\u202fAM – 6:00\u202fPM"
                    )
                ),
                Poi(
                    name = "Lake Tahoe",
                    photo = "",
                    description = "Lake Tahoe is a picturesque alpine lake surrounded by snow-capped peaks, offering a paradise for nature lovers and hikers with its crystal-clear waters and scenic trails.",
                    longitude = -120.0323507,
                    latitude = 39.0968493,
                    tags = listOf("nature", "hiking"),
                    stars = (4.8).toFloat(),
                    address = "Lake Tahoe, United States",
                    phone = "",
                    website = "",
                    schedule = List(0) { "" }
                )
            )
        )
    }

    @GetMapping("/filter-cities/{text}")
    fun filterCities(@PathVariable text: String): List<String> {
        return citiesService.filterCities(text)
    }

    @GetMapping("/city-exists/{city}")
    fun cityExists(@PathVariable city: String): Boolean {
        return citiesService.existsCity(city)
    }
}
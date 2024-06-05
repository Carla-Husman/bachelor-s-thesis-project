package ro.tuiasi.student.carla.proiect.controller

import ro.tuiasi.student.carla.proiect.services.VacationPlannerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.Itinerary
import ro.tuiasi.student.carla.proiect.gateways.places.PlacesApiGateway
import ro.tuiasi.student.carla.proiect.gateways.places.dto.PlaceDetails
import ro.tuiasi.student.carla.proiect.gateways.search.SearchApiGateway
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails
import ro.tuiasi.student.carla.proiect.gateways.webScraping.WebScrapingApiGateway
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput
import ro.tuiasi.student.carla.proiect.models.utils.*
import ro.tuiasi.student.carla.proiect.services.ChatGptService
import ro.tuiasi.student.carla.proiect.services.FilterCitiesService
import ro.tuiasi.student.carla.proiect.services.ImageGeneratorService

@RestController
@RequestMapping("/api/v1/relation-trip")
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
    private val citiesService: FilterCitiesService,
    private val imageGeneratorService: ImageGeneratorService
) {
    @Operation(
        summary = "Vacation Planner",
        description = "This operation returns a personalized vacation plan based on the provided input parameters. "
    )
    @PostMapping("/planner")
    fun vacationPlanner(@RequestBody vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
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
        return placesApiGateway.searchPlace(destination)
    }

    @Operation(
        summary = "Custom Search",
        description = "Performs a custom search for tourist destinations based on the provided prompt. " +
                "The prompt is a search query that may include keywords related to travel destinations and user interests"
    )
    @GetMapping("/custom-search", params = ["prompt"])
    fun customSearch(@RequestParam(required = true) prompt: String): List<SearchDetails> {
        return searchGateway.search(prompt)
    }

    @Operation(
        summary = "Web Scraping with Content Cleanup",
        description = "Retrieves information by performing web scraping on the provided URL. " +
                "The operation includes cleaning up the content by removing JavaScript, HTML and CSS code. "
    )
    @GetMapping("/web-scraping", params = ["url"])
    fun webScraping(@RequestParam(required = true) url: String): String? {
        return webScrapingGateway.getWebScrapingResults(url)
    }

    @Operation(
        summary = "Filter Cities",
        description = "Filters cities based on the provided text. The operation returns a list of cities that match the text."
    )
    @GetMapping("/filter-cities/{text}")
    fun filterCities(@PathVariable text: String): List<String> {
        return citiesService.filterCities(text)
    }

    @Operation(
        summary = "City Exists",
        description = "Checks if a city exists in the database. The operation returns true if the city exists, otherwise false."
    )
    @GetMapping("/city-exists/{city}")
    fun cityExists(@PathVariable city: String): Boolean {
        return citiesService.existsCity(city)
    }

    @GetMapping("/test")
    fun test(): String {
        var text = imageGeneratorService.generateImage("Venice, Italy")
        return text
    }
}
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
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput
import ro.tuiasi.student.carla.proiect.models.utils.Interests
import ro.tuiasi.student.carla.proiect.models.utils.Transport
import ro.tuiasi.student.carla.proiect.services.ChatGptService
import java.util.*

@RestController
@RequestMapping("/api/v1/test")
@Tag(
    name = "Vacation Planner Controller", description = "This provides all operations for managing vacation planner"
)
class VacationPlannerController(
    private val vacationPlannerService: VacationPlannerService,
    private val chatGptService: ChatGptService,
    private val placesApiGateway: PlacesApiGateway,
    private val searchGateway: SearchApiGateway
){
    @Operation(
        summary = "Vacation Planner", description = "This operation return a vacation for a given input"
    )
    @PostMapping("/planner")
    fun vacationPlanner(@RequestBody vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
        /* An input for testing
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

    // a function for testing the chatgpt service
    @GetMapping("/chatgpt-poi")
    fun chatGpt(): Itinerary? {
        return chatGptService.generatePoi(
            city = "Iasi, Romania",
            transport = Transport.WALKING.toString().lowercase(Locale.getDefault()),
            interests = listOf(Interests.ART, Interests.HISTORY, Interests.NATURE),
            otherInterests = null
        )
    }

    @GetMapping("/chatgpt-cities")
    fun chatGptCities(): List<String>? {
        return chatGptService.extractCitiesFromText(
            "Best Romanian fall destinations - RomaniaTourStore\n" +
                    "     Bucharest Tours\n" +
                    "\n" +
                    "Romania Tours\n" +
                    "\n" +
                    "Outdoor Activities\n" +
                    "\n" +
                    "Eastern Europe Tours\n" +
                    "\n" +
                    "Blog\n" +
                    "                     +40 (0) 758 990 033\n" +
                    "\n" +
                    "                                    book@romaniatourstore.com\n" +
                    "         To Search, type and hit enter\n" +
                    "    enesit\n" +
                    "        0\n" +
                    "          Bucharest Tours\n" +
                    "\n" +
                    "Romania Tours\n" +
                    "\n" +
                    "Outdoor Activities\n" +
                    "\n" +
                    "Eastern Europe Tours\n" +
                    "\n" +
                    "Blog\n" +
                    "  Best Romanian fall destinations\n" +
                    "      2 September 2016 \n" +
                    "        Do you feel the need of a getaway, even for just a weekend? Why not take a trip to Romania and see the way the nature is changing colors into the new season. You can opt for an urban adventure in one of Romania’s cities, learn more about the rural life in some of the most beautiful Romanian villages, or if you enjoy active trips, you can go trekking in the mountains until the end of October, considering that the weather is nice.\n" +
                    "\n" +
                    "Why should you choose Romania as a travel fall destination? Because it’s one of the most affordable ones in Europe, it offers a lot of variety in terms of travel destinations and tourist attractions and it’s relatively accessible. Here are some great Romanian travel destinations for the fall season:\n" +
                    "\n" +
                    "Maramures region\n" +
                    "\n" +
                    "The Maramures region is a great option for any fall getaway. The villages scattered on hills that change color in rust hues are a perfect place to relax. You can do hiking, visits to the Merry Cemetery in Sapanta, unique in the world, and to the Barsana and Ieud monasteries. If you decide to go hiking, don’t forget to pack some warm clothes with you and a rain coat.\n" +
                    "\n" +
                    "Sighisoara\n" +
                    "\n" +
                    "If you’re not interested in spending some time in nature, but rather go on a romantic getaway, you can opt for a weekend in Sighisoara. The medieval fortress, included among Business Insider’s European destinations to visit, has some interesting tourist attractions to visit such as the Clock Tower, the Venetian House and the covered stairs that always manage to delight travelers, regardless of how often they would see them and no matter where they come from.\n" +
                    "\n" +
                    "If you take a trip to Sighisoara at the end of October, when Halloween is just around the corner, numerous events and parties are organized in various hot sports of the town to celebrate “the day of the undead.” After all, Vlad Tepes’ home is also located in Sighisoara and you can visit it, as it functions as a thematic restaurant and museum at the same time.\n" +
                    "\n" +
                    "Magura village\n" +
                    "\n" +
                    "Located between Bucegi and Piatra Craiului Mountains, at an altitude of approximatley 1,000 meters, the village of Magura offers breathtaking scenery, especially during the fall season when, nature overwhelms you with all its colors, giving you a nice feeling of tranquility.\n" +
                    "\n" +
                    "What can you do in Magura? Of course, there are a lot of trails you can cover on foot or by bike. The simplest and most satisfying one is a trail to Curmatura and another simple trail, even for beginner is the one to the Zarnesti Steeps.\n" +
                    "\n" +
                    "Brasov city\n" +
                    "\n" +
                    "As we mentioned before, Brasov is a great fall destination, weather you’re travelling for the first time to Romania or you are already familiar with some of the most important destinations.\n" +
                    "\n" +
                    "You can’t pass through Brasov without passing through Sfatului Square (Piata Sfatului). During medieval times, this was the place where Romanian, Saxon and Hungarian merchants used to organize fairs. In the building with an observation tower, also known as Casa Sfatului, there were arranged chamber for magistrates. In time, Casa Sfatului was a city hall and now it functions as a History Museum.\n" +
                    "\n" +
                    "Of course, near the square is the famous Black Church, the largest gothic cathedral from southeastern Europe. The church was built at the end of the fourteenth century as a Catholic church, but after the religious reforms established by Johannes Honterus in Transylvania, it was converted into an Evangelical church.\n" +
                    "The medieval sites also include two remaining gates, Ecaterina Gate and Schei Gate, as well as various medieval towers dedicated to the various guilds.\n" +
                    "\n" +
                    "Omu Peak in the Bucegi Mountains\n" +
                    "\n" +
                    "Reaching the Omu Peak is always interesting, because you have to pass by the Babele and the Sphinx, the mysterious and unique natural rock formations found in the Bucegi Mountains. Some say aliens made them, but that’s a story for another time. After that, every hiker going up on the Bucegi will inevitably rest a little or spend the night at the Omu Hut. The trail offers some breathtaking, picture – perfect landscapes. Also, you shouldn’t be surprised if you encounter some shepherds with their sheep and dogs just casually lounging in the grass.\n" +
                    "\n" +
                    "Two castles: Bran and Peles\n" +
                    "\n" +
                    "Prahova Valley is not only a crowded during winter, when tourists enjoy skiing or practicing other types of winter sports in Sinaia, Busteni, Predeal or Poiana Brasov. This is also a great fall destination if you want to visit some of the most beautiful castles in Europe: Bran and Peles.\n" +
                    "\n" +
                    "Bran Castle is located near Brasov and it’s now practically an amusement park for Dracula’s fans. However, if you’re not into the whole vampire myth thing, you can still visit the rooms of the castle, admire the unique architectural style and various items dating back to centuries ago.\n" +
                    "\n" +
                    "Peles Castle was one of the residences of the Romanian Royal Family in the past and it was built at the desire of the first king, Carol I. The castle houses various art pieces and other valuable items, each of the rooms being decorated in various styles.\n" +
                    "\n" +
                    "You can visit both castles in just one day, by booking our Two Famous Castles tour.\n" +
                    "\n" +
                    "If you’re not convinced yet that Romanian country is a great option for fall destinations, be it weekend getaways or longer trips, take a look at our other trips to Romania and find out what else is there to visit in this beautiful country.\n" +
                    "          fall destinations Romania, Romania getaways, romania tours, Romania weekend trips, travel destinations Romania          \n" +
                    "      0\n" +
                    "                             Hiking trip in Bucegi Mountains\n" +
                    "\n" +
                    "                            €145 \n" +
                    "                             From Budapest via Transylvania\n" +
                    "\n" +
                    "                            €900 \n" +
                    "                             Hiking day – Stan Valley Canyon\n" +
                    "\n" +
                    "                            €95 \n" +
                    "You might also like  \n" +
                    "         5 Famous Romanian women known all around the world\n" +
                    "          15 November 2023      \n" +
                    "         Romania travel guide\n" +
                    "          10 November 2023      \n" +
                    "         Romania Tour Store – a different travel agency\n" +
                    "          24 March 2020      \n" +
                    "  Leave a Reply Cancel replyYour email address will not be published. Required fields are marked *Comment * Name * \n" +
                    "\n" +
                    "Email * \n" +
                    "\n" +
                    "Website \n" +
                    "\n" +
                    "This site is protected by reCAPTCHA and the Google Privacy Policy and Terms of Service apply. \n" +
                    "\n" +
                    "     Current ye@r *\n" +
                    "     Leave this field empty\n" +
                    "Social Media\n" +
                    "MOST WANTED ROMANIA TOURS\n" +
                    "Subjects\n" +
                    "     Bucharest and surroundings\n" +
                    "\n" +
                    " Bucharest Tours\n" +
                    "\n" +
                    " Bukovina and Painted Churches\n" +
                    "\n" +
                    " Carpathian Mountains\n" +
                    "\n" +
                    " Danube Delta and Black Sea\n" +
                    "\n" +
                    " Essential guide Romania\n" +
                    "\n" +
                    " Getting around Romania\n" +
                    "\n" +
                    " Maramures Region\n" +
                    "\n" +
                    " Outdoor Activities\n" +
                    "\n" +
                    " Romania Tours\n" +
                    "\n" +
                    " Tours and activities\n" +
                    "\n" +
                    " Transylvania\n" +
                    "\n" +
                    " Transylvania Tours\n" +
                    "\n" +
                    " Travel to Romania\n" +
                    "\n" +
                    " vlad tepes dracula\n" +
                    "Private guided tours in Romania, including one-day trips and active tours all year round.\n" +
                    "Contact\n" +
                    "18 Theodor Pallady Blvd, M5 Building, Apt. 70, Bucharest\n" +
                    "\n" +
                    "+40 (0) 758 990 033\n" +
                    "\n" +
                    "book@romaniatourstore.com\n" +
                    "Links\n" +
                    "\n" +
                    "Blog\n" +
                    "\n" +
                    "Contact Us\n" +
                    "\n" +
                    "Our Story\n" +
                    "\n" +
                    "Cart\n" +
                    "\n" +
                    "Help Plan My Trip\n" +
                    "\n" +
                    "Frequently asked questions\n" +
                    "\n" +
                    "Terms and Conditions\n" +
                    "\n" +
                    "Tours\n" +
                    "\n" +
                    "Bucharest Tours\n" +
                    "\n" +
                    "Romania Tours\n" +
                    "\n" +
                    "Eastern Europe Tours\n" +
                    "\n" +
                    "Outdoor Activities\n" +
                    "\n" +
                    "Transylvania Explorer Tour\n" +
                    "\n" +
                    "Transylvania biking tour\n" +
                    "       © 2014 - 2023 Romania Tour Store     \n" +
                    "         Terms and Conditions\n" +
                    "\n" +
                    "         Privacy Policy\n" +
                    "\n" +
                    "         Cookies Policy\n" +
                    "  This website uses cookies to improve your experience. We'll assume you're ok with this, but you can opt-out if you wish. Cookie settingsACCEPTPrivacy & Cookies Policy\n" +
                    "   Close\n" +
                    "    Privacy Overview    \n" +
                    "\n" +
                    "     This website uses cookies to improve your experience while you navigate through the website. Out of these cookies, the cookies that are categorized as necessary are stored on your browser as they are essential for the working of basic functionalities of the website. We also use third-party cookies that help us analyze and understand how you use this website. These cookies will be stored in your browser only with your consent. You also have the option to opt-out of these cookies. But opting out of some of these cookies may have an effect on your browsing experience.\n" +
                    "        Necessary       \n" +
                    "         Necessary\n" +
                    "        Always Enabled\n" +
                    "         Necessary cookies are absolutely essential for the website to function properly. This category only includes cookies that ensures basic functionalities and security features of the website. These cookies do not store any personal information.        \n" +
                    "        Non-necessary       \n" +
                    "         Non-necessary\n" +
                    "         Any cookies that may not be particularly necessary for the website to function and is used specifically to collect user personal data via analytics, ads, other embedded contents are termed as non-necessary cookies. It is mandatory to procure user consent prior to running these cookies on your website.        \n" +
                    "               SAVE & ACCEPT"
        )
    }

    // a function for testing the places service
    @GetMapping("/places")
    fun places(): PlaceDetails? {
        return placesApiGateway.searchPlace("Botanical Garden, Iasi, Romania")
    }

    // a function for testing the custom search
    @GetMapping("/custom-search")
    fun customSearch(): List<SearchDetails> {
        return searchGateway.search("Best Cheap Romania travel destinations Autumn intext:(art OR museum OR history)")
    }
}
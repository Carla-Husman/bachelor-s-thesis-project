package ro.tuiasi.student.carla.proiect.services

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.Itinerary
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.ItineraryPoi
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces.IChatGptGateway
import ro.tuiasi.student.carla.proiect.models.utils.*
import ro.tuiasi.student.carla.proiect.services.interfaces.IChatGptService

@Service
class ChatGptService(
    private val chatGptGateway: IChatGptGateway
) : IChatGptService {
    override fun generatePoi(
        city: String,
        gender: Gender?,
        attendant: Attendants?,
        season: Season?,
        transport: Transport?,
        interests: List<Interests>,
        otherInterests: String?
    ): Itinerary? {
        val prompt = "Generate at most 5 points of interests for a tour named $city " + (transport?.let { "$it " }
            ?: "") + (attendant?.let { "$it " } ?: "") + "tour" +
                "for a person interesting in: ${
                    interests.joinToString(
                        " and "
                    )
                }.\n" + (season?.let { "Desired season is $it.\n" } ?: "") +
                (otherInterests?.let { "The additional requirements of the person are: $it.\n" } ?: "") +
                "Generate highlights about the tour." +
                "Generate a description and representatives tags of every POIs.\n" +
                "Parameters of json: tour_name, tour_highlights, points_of_interest (name, description, tags)"

        val content = JSONObject(chatGptGateway.runPrompt(prompt))

        val pointsOfInterest = mutableListOf<ItineraryPoi>()
        for (i in 0 until content.getJSONArray("points_of_interest").length()) {
            pointsOfInterest.add(ItineraryPoi(
                name = content.getJSONArray("points_of_interest").getJSONObject(i).getString("name"),
                description = content.getJSONArray("points_of_interest").getJSONObject(i).getString("description"),
                tags = content.getJSONArray("points_of_interest").getJSONObject(i).getJSONArray("tags")
                    .map { it.toString() }
            ))
        }

        return Itinerary(
            tour_name = content.getString("tour_name"),
            highlights = if (content.has("tour_highlights")) {
                val tourHighlights = content.get("tour_highlights")
                if (tourHighlights is JSONArray) {
                    tourHighlights.map { it.toString() }
                } else {
                    listOf(tourHighlights.toString())
                }
            } else {
                listOf()
            },
            points_of_interest = pointsOfInterest
        )
    }

    override fun extractCitiesFromText(
        text: String,
        destination: String
    ): List<String> {
        var textForDestination = ""
        if (destination != "") {
            textForDestination = " for $destination"
        }
        val prompt = "Extract just the cities $textForDestination from the following text:\n$text \n" +
                "Parameters of json: cities"

        val content = JSONObject(chatGptGateway.runPrompt(prompt))

        val cities = mutableListOf<String>()

        for (i in 0 until content.getJSONArray("cities").length()) {
            cities.add(content.getJSONArray("cities").getString(i))
        }

        return cities
    }

    override fun generatePhoto(destination: String): String {
        val prompt = "Generate a photo for a tour named $destination"
        return chatGptGateway.generateImage(prompt)
    }
}
package ro.tuiasi.student.carla.proiect.services

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.Itinerary
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.ItineraryPoi
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces.IOpenAiGateway
import ro.tuiasi.student.carla.proiect.models.utils.*
import ro.tuiasi.student.carla.proiect.services.interfaces.IChatGptService

@Service
class ChatGptService(
    private val chatGptGateway: IOpenAiGateway
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
        val prompt =
            "Imagine curating an unforgettable journey through the illustrious streets of $city, a place steeped in rich " +
                    "history, vibrant culture, and cherished traditions. Craft a tour experience that captivates the " +
                    "imagination of travelers, offering a glimpse into the soul of this remarkable destination.\n\n" +
                    "Your task is to design a tour itinerary for a person with interests in ${
                        interests.joinToString(
                            " and "
                        )
                    }${if (otherInterests != null) ", $otherInterests" else ""}. Consider the ${
                        transport?.let { "$it, " } ?: ""
                    }${
                        attendant?.let { "$it, " } ?: ""
                    }and plan the perfect ${
                        season?.let { "$it " } ?: "all-season "
                    }adventure, featuring no more than 5 points of interest.\n\n" +
                    "As you construct the tour, delve into the intricacies of each point of interest (POI), infusing the " +
                    "narrative with historical anecdotes, cultural insights, and tales passed down through generations. " +
                    "Let the tour highlights unfold like chapters in a captivating story, each revealing a facet of $city's allure.\n\n" +
                    "Your final output should include a comprehensive description of the tour, intricately woven with vivid " +
                    "imagery and engaging storytelling. Aim for a description that spans approximately one page in length, " +
                    "providing readers with a rich tapestry of details that immerse them in the essence of $city.\n\n" +
                    "For each POI, provide a detailed and long description that transports the reader to that location, " +
                    "evoking the sights, sounds, and sensations unique to $city. Additionally, include representative " +
                    "tags that encapsulate the essence of each POI, ensuring that the tour resonates with a diverse audience.\n\n" +
                    "JSON Parameters:\n" +
                    "- tour_name\n" +
                    "- tour_description\n" +
                    "- tour_highlights\n" +
                    "- points_of_interest (name, description, tags)"

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
}
package ro.tuiasi.student.carla.proiect.services

import org.json.JSONObject
import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.Itinerary
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.ItineraryPoi
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces.IChatGptGateway
import ro.tuiasi.student.carla.proiect.models.utils.Interests
import ro.tuiasi.student.carla.proiect.services.interfaces.IChatGptService

@Service
class ChatGptService(
    private val chatGptGateway: IChatGptGateway
) : IChatGptService {
    override fun generatePoi(
        city: String,
        transport: String?,
        interests: List<Interests>,
        otherInterests: String?
    ): Itinerary? {
        val prompt = "Generate at most 15 points of interests for a tour named $city $transport tour" +
                "for a person interesting in: ${
                    interests.joinToString(
                        " and "
                    )
                }.\n" +
                otherInterests?.let { "The person is also interested in: $it.\n" } +
                "Generate highlights about the tour." +
                "Generate a description and representatives tags of every POIs.\n" +
                "Parameters of json: tour_name, tour_highlights, points_of_interest (name, description, tags)"

        val content = JSONObject(chatGptGateway.runPrompt(prompt))

        val pointsOfInterest = mutableListOf<ItineraryPoi>()
        for (i in 0 until content.getJSONArray("points_of_interest").length()) {
            pointsOfInterest.add( ItineraryPoi(
                name = content.getJSONArray("points_of_interest").getJSONObject(i).getString("name"),
                description = content.getJSONArray("points_of_interest").getJSONObject(i).getString("description"),
                tags = content.getJSONArray("points_of_interest").getJSONObject(i).getJSONArray("tags").map { it.toString() }
            ))
        }

        return Itinerary(
            tour_name = content.getString("tour_name"),
            highlights = content.getJSONArray("tour_highlights").map { it.toString() },
            points_of_interest = pointsOfInterest
        )
    }

    override fun extractCitiesFromText(
        text: String
    ): List<String> {
        val prompt = "Extract just the cities from the following text:\n$text"

        val content = JSONObject(chatGptGateway.runPrompt(prompt))

        val cities = mutableListOf<String>()

        for (i in 0 until content.getJSONArray("cities").length()) {
            cities.add(content.getJSONArray("cities").getString(i))
        }

        return cities
    }
}
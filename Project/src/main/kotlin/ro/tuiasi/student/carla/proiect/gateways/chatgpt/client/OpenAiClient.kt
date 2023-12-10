package ro.tuiasi.student.carla.proiect.gateways.chatgpt.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.json.JSONObject
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.*

@Service
class OpenAiClient (
    private val restTemplate: RestTemplate,

    @Value("\${integration.gateway.chatgpt.model}")
    private val model: String,

    @Value("\${integration.gateway.chatgpt.base-uri}")
    private val endpoint: String,
){
    fun chatConversation(message: String): Itinerary? {
        val request = ChatRequest(
            model = model
        )

        request.messages.add(Message(
            role = "user",
            content = message
        ))

        try {
            val response = restTemplate.postForObject(
                endpoint,
                request,
                ChatResponse::class.java
            )

            if (response?.choices == null || response.choices.isEmpty()) {
                return null
            }

            val itinerary = JSONObject(response.choices[0].message.content)
            println(response.choices[0].message.content)
            val pointsOfInterest = mutableListOf<ItineraryPoi>()
            for (i in 0 until itinerary.getJSONArray("points_of_interest").length()) {
                pointsOfInterest.add( ItineraryPoi(
                    name = itinerary.getJSONArray("points_of_interest").getJSONObject(i).getString("name"),
                    description = itinerary.getJSONArray("points_of_interest").getJSONObject(i).getString("description"),
                    tags = itinerary.getJSONArray("points_of_interest").getJSONObject(i).getJSONArray("tags").map { it.toString() }
                ))
            }

            return Itinerary(
                tour_name = itinerary.getString("tour_name"),
                highlights = itinerary.getJSONArray("tour_highlights").map { it.toString() },
                points_of_interest = pointsOfInterest
            )

        } catch (e: Exception) {
            println(e.message)
            return null
        }
    }
}
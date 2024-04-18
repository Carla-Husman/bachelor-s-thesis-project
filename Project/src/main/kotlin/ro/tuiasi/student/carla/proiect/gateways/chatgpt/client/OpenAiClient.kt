package ro.tuiasi.student.carla.proiect.gateways.chatgpt.client

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.*

@Service
class OpenAiClient(
    private val restTemplate: RestTemplate,

    @Value("\${integration.gateway.chatgpt.model}")
    private val modelGpt: String,

    @Value("\${integration.gateway.dalle.model}")
    private val modelDalle: String,

    @Value("\${integration.gateway.chatgpt.base-uri}")
    private val endpointGpt: String,

    @Value("\${integration.gateway.dalle.base-uri}")
    private val endpointDalle: String
) {
    fun chatConversation(message: String): String? {
        val request = ChatRequest(
            model = modelGpt
        )

        request.messages.add(
            Message(
                role = "user",
                content = message
            )
        )

        val response = restTemplate.postForObject(
            endpointGpt,
            request,
            ChatResponse::class.java
        )

        if (response?.choices == null || response.choices.isEmpty()) {
            throw Exception("ChatGpt response is null or empty.")
        }

        return response.choices[0].message.content
    }

    fun imageGeneration(message: String): String {
        val request = DalleRequest(
            model = modelDalle,
            prompt = message
        )

        val response = restTemplate.postForObject(
            endpointDalle,
            request,
            String::class.java
        )

        val jsonResponse = JSONObject(response)

        return jsonResponse.getJSONArray("data").getJSONObject(0).getString("url") ?: ""
    }
}
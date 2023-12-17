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
    fun chatConversation(message: String): String? {
        val request = ChatRequest(
            model = model
        )

        request.messages.add(Message(
            role = "user",
            content = message
        ))

        val response = restTemplate.postForObject(
            endpoint,
            request,
            ChatResponse::class.java
        )

        if (response?.choices == null || response.choices.isEmpty()) {
            throw Exception("ChatGpt response is null or empty.")
        }

        return response.choices[0].message.content
    }
}
package ro.tuiasi.student.carla.proiect.gateways.chatgpt.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.ChatRequest
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.ChatResponse
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.Message
import org.json.JSONObject

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

        try {
            val response = restTemplate.postForObject(
                endpoint,
                request,
                ChatResponse::class.java
            )

            if (response?.choices == null || response.choices.isEmpty()) {
                return "No response"
            }
            return response.choices[0].message.content

        } catch (e: Exception) {
            return "Exception"
        }
    }
}
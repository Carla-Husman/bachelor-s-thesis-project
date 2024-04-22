package ro.tuiasi.student.carla.proiect.gateways.chatgpt

import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.client.OpenAiClient
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces.IOpenAiGateway

@Service
class OpenAiGateway(
    val openAiClient: OpenAiClient
) : IOpenAiGateway {

    override fun runPrompt(message: String): String? {
        return openAiClient.chatConversation(message)
    }

    override fun generateImage(message: String): String {
        return openAiClient.imageGeneration(message)
    }
}
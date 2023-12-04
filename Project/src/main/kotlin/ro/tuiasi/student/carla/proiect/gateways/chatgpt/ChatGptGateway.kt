package ro.tuiasi.student.carla.proiect.gateways.chatgpt

import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.client.OpenAiClient
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces.IChatGptGateway

@Service
class ChatGptGateway (
    val openAiClient: OpenAiClient
) : IChatGptGateway {

    override fun runPrompt(message: String): String? {
        return openAiClient.chatConversation(message)
    }
}
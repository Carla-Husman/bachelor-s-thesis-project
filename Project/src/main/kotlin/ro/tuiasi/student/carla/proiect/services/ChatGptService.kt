package ro.tuiasi.student.carla.proiect.services

import org.springframework.stereotype.Service
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
        interests: List<Interests>
    ): String? {
        val prompt = "Generate at most 15 points of interests for a tour named $city $transport tour" +
                "for a persont interesing in: ${
                    interests.joinToString(
                        " and "
                    )
                }.\n" +
                "Generate highlights about the tour." +
                "Generate a description and representatives tags of every POIs."

        return chatGptGateway.runPrompt(prompt)
    }
}
package ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto

class ChatResponse {
    val choices: List<Choice> = mutableListOf()

    class Choice {
         val index = 0
         val message: Message = Message(
             role = "assistant",
             content = ""
         )
    }
}
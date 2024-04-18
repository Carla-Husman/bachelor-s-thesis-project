package ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces

interface IChatGptGateway {
    fun runPrompt(message: String): String?

    fun generateImage(message: String): String
}
package ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces

interface IOpenAiGateway {
    fun runPrompt(message: String): String?

    fun generateImage(message: String): String
}
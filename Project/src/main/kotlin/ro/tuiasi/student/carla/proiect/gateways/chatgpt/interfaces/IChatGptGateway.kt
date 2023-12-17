package ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces

import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.Itinerary

interface IChatGptGateway {
    fun runPrompt(message: String): String?

}
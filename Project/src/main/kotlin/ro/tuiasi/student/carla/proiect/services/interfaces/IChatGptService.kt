package ro.tuiasi.student.carla.proiect.services.interfaces

import ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto.Itinerary
import ro.tuiasi.student.carla.proiect.models.utils.*

interface IChatGptService {
    fun generatePoi(
        city: String,
        gender: Gender?,
        attendant: Attendants?,
        season: Season?,
        transport: Transport?,
        interests: List<Interests>,
        otherInterests: String?
    ): Itinerary?

    fun extractCitiesFromText(text: String, destination: String): List<String>

    fun generatePhoto(destination: String): String
}
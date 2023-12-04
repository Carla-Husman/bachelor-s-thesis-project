package ro.tuiasi.student.carla.proiect.services.interfaces

import ro.tuiasi.student.carla.proiect.models.utils.Interests

interface IChatGptService {
    fun generatePoi(city: String, transport: String?, interests: List<Interests>): String?
}
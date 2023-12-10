package ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto

class Itinerary(
    val tour_name: String,
    val highlights: List<String>,
    val points_of_interest: List<ItineraryPoi>
)
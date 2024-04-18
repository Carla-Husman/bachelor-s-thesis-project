package ro.tuiasi.student.carla.proiect.gateways.places.dto

class PlaceDetails(
    var placeId: String,
    var photoReference: String,
    var placeName: String,
    var placeAddress: String,
    var latitude: Double,
    var longitude: Double,
    var rating: Float,
    var phone: String?,
    var website: String?,
    var schedule: MutableList<Any>
)
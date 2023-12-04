package ro.tuiasi.student.carla.proiect.gateways.places.interfaces

import org.json.JSONObject
import ro.tuiasi.student.carla.proiect.gateways.places.dto.PlaceDetails

interface IPlacesApiGateway {
    fun searchPlace(placeName: String): PlaceDetails?
}
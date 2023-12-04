package ro.tuiasi.student.carla.proiect.gateways.places

import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.places.client.PlacesApiClient
import ro.tuiasi.student.carla.proiect.gateways.places.dto.PlaceDetails
import ro.tuiasi.student.carla.proiect.gateways.places.interfaces.IPlacesApiGateway

@Service
class PlacesApiGateway(
    private val googlePlacesClient: PlacesApiClient
) : IPlacesApiGateway {
    override fun searchPlace(placeName: String): PlaceDetails? {
        return googlePlacesClient.textSearch(placeName)
    }
}
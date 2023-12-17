package ro.tuiasi.student.carla.proiect.gateways.places.client

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import ro.tuiasi.student.carla.proiect.gateways.places.dto.PlaceDetails

@Service
class PlacesApiClient (
    var restTemplate: RestTemplate,

    @Value("\${integration.gateway.maps.places.api-key}")
    val placesApiKey: String,

    @Value("\${integration.gateway.maps.places.endpoint-text-search}")
    val searchTextEndpoint: String,

    @Value("\${integration.gateway.maps.places.endpoint-details}")
    val detailsEndpoint: String
){
    fun textSearch(placeName: String): PlaceDetails {
        restTemplate = RestTemplate()

        val uri = UriComponentsBuilder
            .fromUriString(searchTextEndpoint)
            .queryParam("query", placeName)
            .queryParam("key", placesApiKey)
            .build()
            .toUri()

        val textSearch = JSONObject(restTemplate.getForObject(uri, String::class.java)).getJSONArray("results").getJSONObject(0)

        val placeDetails = placeDetails(textSearch.getString("place_id"))

        return PlaceDetails(
            placeId = textSearch.getString("place_id"),
            //photoReference = textSearch.getJSONArray("photos").getJSONObject(0).getString("photo_reference"),
            placeName = textSearch.getString("name"),
            placeAddress = textSearch.getString("formatted_address"),
            latitude = textSearch.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
            longitude = textSearch.getJSONObject("geometry").getJSONObject("location").getDouble("lng"),
            rating = if (placeDetails.has("rating")){
                placeDetails.getDouble("rating").toFloat()
            } else{
                0.0.toFloat()
            },
            phone = if (placeDetails.has("international_phone_number")){
                placeDetails.getString("international_phone_number")
            } else{
                ""
            },
            website = if (placeDetails.has("website")){
                placeDetails.getString("website")
            } else{
                ""
            },
            schedule = if (placeDetails.has("opening_hours")){
                placeDetails.getJSONObject("opening_hours").getJSONArray("weekday_text").toMutableList()
            } else{
                mutableListOf()
            }
        )
    }

    private fun placeDetails (placeId: String) : JSONObject {
        restTemplate = RestTemplate()

        val uri = UriComponentsBuilder
            .fromUriString(detailsEndpoint)
            .queryParam("place_id", placeId)
            .queryParam("key", placesApiKey)
            .build()
            .toUri()

        return JSONObject(restTemplate.getForObject(uri, String::class.java)).getJSONObject("result")
    }
}
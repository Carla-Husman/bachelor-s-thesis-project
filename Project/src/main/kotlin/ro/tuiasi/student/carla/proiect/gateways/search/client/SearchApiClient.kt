package ro.tuiasi.student.carla.proiect.gateways.search.client

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails

@Service
class SearchApiClient (
    private var restTemplate: RestTemplate,

    @Value("\${integration.gateway.google-search.api-key}")
    private val searchApiKey: String,

    @Value("\${integration.gateway.google-search.endpoint}")
    private val searchEndpoint: String,

    @Value("\${integration.gateway.google-search.cx}")
    private val cx: String
) {
    fun search (query: String) : List<SearchDetails> {
        restTemplate = RestTemplate()

        val uri = UriComponentsBuilder
            .fromUriString(searchEndpoint)
            .queryParam("key", searchApiKey)
            .queryParam("cx", cx)
            .queryParam("q", query)
            .queryParam("safe", 1)
            .build()
            .toUri()

        val search = JSONObject(restTemplate.getForObject(uri, String::class.java))

        val totalResults = search.getJSONObject("searchInformation").getInt("totalResults")

        val searchDetailsList = mutableListOf<SearchDetails>()

        if (totalResults > 0){
            val items = search.getJSONArray("items")

            for (i in 0 until items.length()) {
                var cacheId = ""

                try{
                    cacheId = items.getJSONObject(i).getString("cacheId")
                }
                catch (e: Exception){
                    println(e.message)
                }

                searchDetailsList.add(SearchDetails(
                    title = items.getJSONObject(i).getString("title"),
                    link = items.getJSONObject(i).getString("link"),
                    cachedId = cacheId)
                )
            }
        }

        return searchDetailsList
    }
}
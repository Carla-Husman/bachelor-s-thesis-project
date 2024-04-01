package ro.tuiasi.student.carla.proiect.gateways.search.client

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.expression.common.ExpressionUtils.toInt
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails

@Service
class SearchApiClient(
    private var restTemplate: RestTemplate,

    @Value("\${integration.gateway.google-search.api-key}")
    private val searchApiKey: String,

    @Value("\${integration.gateway.google-search.endpoint}")
    private val searchEndpoint: String,

    @Value("\${integration.gateway.google-search.cx}")
    private val cx: String
) {
    fun search(query: String): List<SearchDetails> {
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

        val totalResults = if (search.getJSONObject("searchInformation").get("totalResults") is Int) {
            search.getJSONObject("searchInformation").getInt("totalResults").toLong()
        } else {
            search.getJSONObject("searchInformation").getString("totalResults").toLong()
        }

        val searchDetailsList = mutableListOf<SearchDetails>()

        if (totalResults > 0) {
            val items = search.getJSONArray("items")
            for (i in 0 until items.length()) {
                searchDetailsList.add(
                    SearchDetails(
                        title = items.getJSONObject(i).getString("title"),
                        link = items.getJSONObject(i).getString("link"),
                        cachedId = if (items.getJSONObject(i).has("cacheId")) {
                            items.getJSONObject(i).getString("cacheId")
                        } else {
                            ""
                        }
                    )
                )
            }
            return searchDetailsList
        }

        throw HttpClientErrorException(HttpStatus.NO_CONTENT, "No results found for the given query.")
    }
}
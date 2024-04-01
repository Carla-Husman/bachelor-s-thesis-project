package ro.tuiasi.student.carla.proiect.gateways.search

import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.search.client.SearchApiClient
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails
import ro.tuiasi.student.carla.proiect.gateways.search.interfaces.ISearchApiGateway

@Service
class SearchApiGateway(
    private val searchClient: SearchApiClient
) : ISearchApiGateway {
    override fun search(query: String): List<SearchDetails> {
        return searchClient.search(query)
    }
}
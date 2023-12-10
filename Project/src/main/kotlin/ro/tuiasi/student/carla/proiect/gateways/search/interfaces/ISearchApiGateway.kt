package ro.tuiasi.student.carla.proiect.gateways.search.interfaces

import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails

interface ISearchApiGateway {
    fun search(query: String): List<SearchDetails>
}
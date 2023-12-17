package ro.tuiasi.student.carla.proiect.services.interfaces

import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput

interface ICustomSearchService {
    fun search(searchPrompt: String): List<SearchDetails>

    fun generatePromptCustomSearch(vacationPlannerInput: VacationPlannerInput): String
}
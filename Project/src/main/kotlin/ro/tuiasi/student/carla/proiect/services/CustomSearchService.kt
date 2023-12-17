package ro.tuiasi.student.carla.proiect.services

import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.search.SearchApiGateway
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.services.interfaces.ICustomSearchService

@Service
class CustomSearchService(
    private val searchApiGateway: SearchApiGateway
) : ICustomSearchService {
    override fun search(searchPrompt: String): List<SearchDetails> {
        // an empty search prompt means that we have a city as destination
        if (searchPrompt == "") {
            return listOf()
        }

        // otherwise, we search on Google because we have a continent or a country as destination
        return searchApiGateway.search(searchPrompt)
    }

    override fun generatePromptCustomSearch(vacationPlannerInput: VacationPlannerInput): String {

        // if destination is null, we will search for the best trip in the world
        if (vacationPlannerInput.destination == null) {
            return "Best" + (vacationPlannerInput.budget?.let { " $it" } ?: "") + " trip destination in whole the world" +
                    (vacationPlannerInput.attendant?.let { " for $it" } ?: "") +
                    (vacationPlannerInput.season?.let { ", $it" } ?: "") +
                    " intext:(${vacationPlannerInput.interests.joinToString(" OR ")})"
        }

        // if destination is a continent or a country
        else if (vacationPlannerInput.destination.matches(Regex("^(Asia|Africa|North America|South America|Antarctica|Europe|Australia)\$")) ||
            !vacationPlannerInput.destination.matches(Regex("^(([a-zA-Z\\u0080-\\u024F ]|(?:[-'’.`]))*, ([a-zA-Z\\u0080-\\u024F ]|(?:[-'’.`]))*)\$"))){
            return "Best" + (vacationPlannerInput.budget?.let { " $it" } ?: "") + " trip destination in ${vacationPlannerInput.destination}" +
                    (vacationPlannerInput.attendant?.let { " for $it" } ?: "") +
                    (vacationPlannerInput.season?.let { ", $it" } ?: "") +
                    " intext:(${vacationPlannerInput.interests.joinToString(" OR ")})"
        }

        // destination is a city
        return ""
    }
}
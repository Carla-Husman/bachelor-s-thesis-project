package ro.tuiasi.student.carla.proiect.services

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import ro.tuiasi.student.carla.proiect.gateways.search.SearchApiGateway
import ro.tuiasi.student.carla.proiect.gateways.search.dto.SearchDetails
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.utils.Budget
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

        // otherwise, we search on Google because we have a continent, a country or nothing as destination
        return searchApiGateway.search(searchPrompt)
    }

    override fun generatePromptCustomSearch(vacationPlannerInput: VacationPlannerInput): String {
        val budget = vacationPlannerInput.budget?.let {
            when(it){
                Budget.CHEAP, Budget.AFFORDABLE -> "trip, countries or cities close to ${vacationPlannerInput.startingPoint} "
                else -> "$it trip destination in the world "
            }
        } ?: "destination in the whole world "
        // if destination is empty, we will search for the best trip in the world
        if (vacationPlannerInput.destination == "") {
            return "Best " + (vacationPlannerInput.attendant?.let { "$it " } ?: "") + budget +
                    (vacationPlannerInput.season?.let { ", $it " } ?: "") +
                    "intext:(${vacationPlannerInput.interests.joinToString(" OR ")})"
        }
        // if destination is a continent or a country
        else if (vacationPlannerInput.destination.matches(Regex("^(Asia|Africa|North America|South America|Antarctica|Europe|Australia)\$")) ||
            vacationPlannerInput.destination.matches(Regex("^[a-zA-Z\\s]+\$"))) {
            return "Best " + (vacationPlannerInput.attendant?.let { "$it " } ?: "") +
                    (vacationPlannerInput.budget?.let { "$it " } ?: "") +
                    "trip destination in ${vacationPlannerInput.destination} " +
                    (vacationPlannerInput.season?.let { ", $it " } ?: "") +
                    "intext:(${vacationPlannerInput.interests.joinToString(" OR ")})"
        }
        // if destination is a city
        else if (vacationPlannerInput.destination.matches(Regex("^[a-zA-ZÀ-ÿĀ-žǍ-ȳ'’.`\\s]+,\\s?[a-zA-ZÀ-ÿĀ-žǍ-ȳ'’.`\\s]+\$"))) {
            return ""
        }

        throw HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Format of the destination is not valid.")
    }
}
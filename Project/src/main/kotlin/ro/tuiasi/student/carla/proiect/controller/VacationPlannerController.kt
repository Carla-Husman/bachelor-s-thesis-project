package ro.tuiasi.student.carla.proiect.controller

import ro.tuiasi.student.carla.proiect.services.VacationPlannerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput
import ro.tuiasi.student.carla.proiect.models.utils.Interests
import ro.tuiasi.student.carla.proiect.models.utils.Transport
import ro.tuiasi.student.carla.proiect.services.ChatGptService
import java.util.*

@RestController
@RequestMapping("/api/v1/test")
@Tag(
    name = "Vacation Planner Controller", description = "This provides all operations for managing vacation planner"
)
class VacationPlannerController(
    private val vacationPlannerService: VacationPlannerService,
    private val chatGptService: ChatGptService
){
    @Operation(
        summary = "Vacation Planner", description = "This operation return a vacation for a given input"
    )
    @PostMapping("/planner")
    fun vacationPlanner(@RequestBody vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
        return vacationPlannerService.vacationPlanner(vacationPlannerInput)
    }

    // a function for testing the chatgpt service
    @GetMapping
    fun chatGpt(): String? {
        return chatGptService.generatePoi(
            city = "Iasi, Romania",
            transport = Transport.WALKING.toString().lowercase(Locale.getDefault()),
            interests = listOf(Interests.ART, Interests.HISTORY, Interests.NATURE)
        )
    }
}
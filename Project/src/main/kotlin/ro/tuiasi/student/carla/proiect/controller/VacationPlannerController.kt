package ro.tuiasi.student.carla.proiect.controller

import ro.tuiasi.student.carla.proiect.services.VacationPlannerService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput

@RestController
@RequestMapping("/api/v1/test")
@Tag(
    name = "Vacation Planner Controller", description = "This provides all operations for managing vacation planner"
)
class VacationPlannerController(
    private val vacationPlannerService: VacationPlannerService
){
    @Operation(
        summary = "Vacation Planner", description = "This operation return a vacation for a given input"
    )
    @PostMapping("/planner")
    fun vacationPlanner(@RequestBody vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
        return vacationPlannerService.vacationPlanner(vacationPlannerInput)
    }
}
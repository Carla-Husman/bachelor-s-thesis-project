package ro.tuiasi.student.carla.proiect.services.interfaces

import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput

interface IVacationPlannerService {
    fun vacationPlanner(vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput
}
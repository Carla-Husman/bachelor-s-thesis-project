package ro.tuiasi.student.carla.proiect.models

import ro.tuiasi.student.carla.proiect.models.utils.Attendants
import ro.tuiasi.student.carla.proiect.models.utils.Budget
import ro.tuiasi.student.carla.proiect.models.utils.Season
import ro.tuiasi.student.carla.proiect.models.utils.Transport

data class VacationPlannerOutput(
    val photo: String,
    val name: String,
    val destination: String,
    val startingPoint: String,
    val season: Season?,
    val attendant: Attendants?,
    val transport: Transport?,
    val budget: Budget?,
    val distance: Double,
    val poisNumber: Int,
    val highlights: List<String>,
    val pois: List<Poi>
)
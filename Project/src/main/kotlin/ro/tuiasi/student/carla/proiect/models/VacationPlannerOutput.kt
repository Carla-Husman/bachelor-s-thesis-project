package ro.tuiasi.student.carla.proiect.models

import ro.tuiasi.student.carla.proiect.models.utils.Season

data class VacationPlannerOutput(
    val photo: String,
    val name: String,
    val destination: String,
    val season: Season?,
    val attendant: String?,
    val distance: Double,
    val poisNumber: Int,
    val highlights: List<String>,
    val pois: List<Poi>
)
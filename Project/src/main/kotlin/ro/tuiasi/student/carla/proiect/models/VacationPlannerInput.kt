package ro.tuiasi.student.carla.proiect.models

import ro.tuiasi.student.carla.proiect.models.utils.*

data class VacationPlannerInput(
    val destination: String?,
    val startingPoint: String,
    val age: Int?,
    val gender: Gender?,
    val attendant: String?,
    val season: Season?,
    val transport: Transport?,
    val budget: Budget?,
    val interests: List<Interests>,
    val otherInterests: String?
)
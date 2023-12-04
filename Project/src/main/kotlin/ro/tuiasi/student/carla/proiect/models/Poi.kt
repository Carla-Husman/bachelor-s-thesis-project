package ro.tuiasi.student.carla.proiect.models

data class Poi(
    val name: String,
    val photo: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val tags: List<String>,
    val stars: Double,
    val address: String,
    val phone: String?,
    val website: String?,
    val schedule: String?,
)
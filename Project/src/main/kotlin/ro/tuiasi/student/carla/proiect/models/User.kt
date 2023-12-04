package ro.tuiasi.student.carla.proiect.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val avatar: String?,
    val location: String?,
    val longitude: Double,
    val latitude: Double,
    val year: Int?,
    val gender: String?
)
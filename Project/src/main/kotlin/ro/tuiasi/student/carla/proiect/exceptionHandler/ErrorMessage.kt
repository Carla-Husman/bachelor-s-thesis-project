package ro.tuiasi.student.carla.proiect.exceptionHandler

data class ErrorMessage(
    val statusCode: Int,
    val message: String,
    val description: String,
)

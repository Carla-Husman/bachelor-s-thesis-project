package ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto

class ResponseFormat(
    val type: String
) {
    constructor() : this(
        type = "json_object"
    )
}
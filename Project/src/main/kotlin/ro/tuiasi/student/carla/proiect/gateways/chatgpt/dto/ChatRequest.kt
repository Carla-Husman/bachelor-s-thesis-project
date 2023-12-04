package ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto

class ChatRequest (
    val model: String,
    val messages: MutableList<Message>,
    val temperature: Double = 0.9
) {
    constructor(model: String) : this(
        model = model,
        messages = mutableListOf(
            Message("system", "You are a helpful tourist guide designed to output JSON.")
        ).toMutableList()
    )
}
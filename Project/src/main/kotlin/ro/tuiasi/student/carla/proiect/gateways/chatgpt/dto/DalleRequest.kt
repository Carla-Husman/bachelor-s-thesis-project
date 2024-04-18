package ro.tuiasi.student.carla.proiect.gateways.chatgpt.dto

class DalleRequest(
    val model: String,
    val prompt: String,
    val n: Int,
    val size: String,
    val quality: String
) {
    constructor(model: String, prompt: String) : this(
        model = model,
        prompt = prompt,
        n = 1,
        size = "1024x1024",
        quality = "standard"
    )
}
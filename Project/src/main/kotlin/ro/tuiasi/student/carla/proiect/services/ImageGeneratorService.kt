package ro.tuiasi.student.carla.proiect.services

import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.chatgpt.interfaces.IOpenAiGateway
import ro.tuiasi.student.carla.proiect.services.interfaces.IImageGeneratorService
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

@Service
class ImageGeneratorService(
    private val dalleGateway: IOpenAiGateway
) : IImageGeneratorService {
    override fun generateImage(itineraryName: String): String {
        val prompt = "Generate a photo for a tour named $itineraryName"
        val imageUrl = dalleGateway.generateImage(prompt)

        // Reading the image from the URL
        val inputImage: BufferedImage = ImageIO.read(URL(imageUrl))

        // Resizing the image
        val outputImage: Image = inputImage.getScaledInstance(400, 300, Image.SCALE_SMOOTH)
        val resizedImage = BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB)
        val graphics2D = resizedImage.createGraphics()
        graphics2D.drawImage(outputImage, 0, 0, null)
        graphics2D.dispose()

        // Converting the image to base64
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(resizedImage, "jpg", outputStream)
        val bytes = outputStream.toByteArray()
        val base64Image = Base64.getEncoder().encodeToString(bytes)

        return "data:image/jpg;base64,$base64Image"
    }
}
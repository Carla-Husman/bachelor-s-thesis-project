package ro.tuiasi.student.carla.proiect.gateways.webScraping

import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.gateways.webScraping.client.WebScrapingApiClient
import ro.tuiasi.student.carla.proiect.gateways.webScraping.interfaces.IWebScrapingApiGateway

@Service
class WebScrapingApiGateway (
    private val webScrapingClient: WebScrapingApiClient
) : IWebScrapingApiGateway {
    override fun getWebScrapingResults(url: String): String? {
        return webScrapingClient.getContentFromUrl(url)
    }
}
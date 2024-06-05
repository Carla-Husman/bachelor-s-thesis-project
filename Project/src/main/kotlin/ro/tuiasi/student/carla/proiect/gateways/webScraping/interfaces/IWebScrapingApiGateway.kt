package ro.tuiasi.student.carla.proiect.gateways.webScraping.interfaces

interface IWebScrapingApiGateway {
    fun getWebScrapingResults(url: String): String
}
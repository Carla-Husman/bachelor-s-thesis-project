package ro.tuiasi.student.carla.proiect.gateways.webScraping.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class WebScrapingApiClient(
    var restTemplate: RestTemplate,

    @Value("\${integration.gateway.web-scraping.endpoint}")
    private val webScrapingApiEndpoint: String,

    @Value("\${integration.gateway.web-scraping.api-key}")
    private val webScrapingApiKey: String,

    @Value("\${integration.gateway.web-scraping.render-js}")
    private val webScrapingRenderJs: String,

    @Value("\${integration.gateway.web-scraping.proxy-type}")
    private val webScrapingProxyType: String,
) {
    fun getContentFromUrl(url: String): String? {
        restTemplate = RestTemplate()
        val uri = UriComponentsBuilder
            .fromUriString(webScrapingApiEndpoint)
            .queryParam("url", url)
            .queryParam("render_js", webScrapingRenderJs)
            .queryParam("proxy_type", webScrapingProxyType)
            .queryParam("api_key", webScrapingApiKey)
            .build()
            .toUri()

        val content: String = restTemplate.getForObject(uri, String::class.java).toString()

        // Remove JavaScript code (comments and <script> tags)
        val noJavaScript =
            content.replace(Regex("/\\*.*?\\*/|//.*?\n|<script.*?</script>", RegexOption.DOT_MATCHES_ALL), "")

        // Remove CSS code (comments and <style> tags)
        val noCss =
            noJavaScript.replace(Regex("/\\*.*?\\*/|//.*?\n|<style.*?</style>", RegexOption.DOT_MATCHES_ALL), "")

        // Remove HTML code (tags)
        val noHtml = noCss.replace(Regex("<[^>]*>", RegexOption.DOT_MATCHES_ALL), "")

        // Remove useless spaces, tabs, empty lines between paragraphs
        val noSpaces = noHtml.replace(Regex("\\s*\n\\s*"), "\n").trim()

        // ?
        return noSpaces.replace(Regex("[a-zA-Z0-9-]+?\\{.*?\\}.*"), " ").trim()
    }
}
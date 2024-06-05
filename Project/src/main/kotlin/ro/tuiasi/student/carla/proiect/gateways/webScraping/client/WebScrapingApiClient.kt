package ro.tuiasi.student.carla.proiect.gateways.webScraping.client

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


@Service
class WebScrapingApiClient(
    private var restTemplate: RestTemplate,

    @Value("\${integration.gateway.web-scraping.endpoint}")
    private val webScrapingApiEndpoint: String,

    @Value("\${integration.gateway.web-scraping.api-key}")
    private val webScrapingApiKey: String,

    @Value("\${integration.gateway.web-scraping.render-js}")
    private val webScrapingRenderJs: String,

    @Value("\${integration.gateway.web-scraping.proxy-type}")
    private val webScrapingProxyType: String,
) {
    init {
        restTemplate = RestTemplate()
        restTemplate.interceptors.add(UserAgentInterceptor())
    }

    fun getContentFromUrl(url: String): String {
        try {
            val uri = UriComponentsBuilder
                .fromUriString(webScrapingApiEndpoint)
                .queryParam("url", url)
                .queryParam("render_js", webScrapingRenderJs)
                .queryParam("proxy_type", webScrapingProxyType)
                .queryParam("api_key", webScrapingApiKey)
                .build()
                .toUri()

            val content: String = restTemplate.getForObject(uri, String::class.java).toString()
            val doc: Document = Jsoup.parse(content)

            return doc.body().text()
        } catch (e: Exception) {
            return ""
        }
    }

    private class UserAgentInterceptor : ClientHttpRequestInterceptor {
        override fun intercept(
            request: org.springframework.http.HttpRequest,
            body: ByteArray,
            execution: ClientHttpRequestExecution
        ): ClientHttpResponse {
            request.headers[HttpHeaders.USER_AGENT] =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.1 Safari/537.36"
            return execution.execute(request, body)
        }
    }
}
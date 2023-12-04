package ro.tuiasi.student.carla.proiect.gateways

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.util.Timeout
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

class GatewayConfiguration (
    private val apiKey: String,
    private val numberOfConnectionsPool: Int,
    private val numberOfConnectionsPoolPerRoute: Int,
    private val socketTimeout: Int,
    private val connectionTimeout: Int,
    private val connectionRequestTimeout: Int,
){
    fun restTemplate(): RestTemplate {
        val connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnTotal(numberOfConnectionsPool)
            .setMaxConnPerRoute(numberOfConnectionsPoolPerRoute)
            .setDefaultConnectionConfig(
                ConnectionConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(connectionTimeout.toLong()))
                    .setSocketTimeout(Timeout.ofSeconds(socketTimeout.toLong()))
                    .build()
            )
            .build()

        val requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.ofSeconds(connectionRequestTimeout.toLong()))
            .build()

        val httpClient = HttpClientBuilder.create()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build()

        val clientHttpRequestFactory = HttpComponentsClientHttpRequestFactory(httpClient)

        val restTemplate = RestTemplate(clientHttpRequestFactory)
        restTemplate.interceptors.add(ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray?, execution: ClientHttpRequestExecution ->
            request.headers.add("Authorization", "Bearer $apiKey")
            execution.execute(request, body!!)
        })

        return restTemplate
    }
}
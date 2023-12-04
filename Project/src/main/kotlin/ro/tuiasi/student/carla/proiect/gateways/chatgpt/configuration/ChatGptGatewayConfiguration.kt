package ro.tuiasi.student.carla.proiect.gateways.chatgpt.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import ro.tuiasi.student.carla.proiect.gateways.GatewayConfiguration

@Configuration
class ChatGptGatewayConfiguration(
    @Value("\${integration.gateway.chatgpt.number-of-connections-pool}")
    private val numberOfConnectionsPool: Int,

    @Value("\${integration.gateway.chatgpt.number-of-connections-pool-per-route}")
    private val numberOfConnectionsPoolPerRoute: Int,

    @Value("\${integration.gateway.chatgpt.timeouts.rest.socket-timeout}")
    private val socketTimeout: Int,

    @Value("\${integration.gateway.chatgpt.timeouts.rest.connection-timeout}")
    private val connectionTimeout: Int,

    @Value("\${integration.gateway.chatgpt.timeouts.rest.connection-request-timeout}")
    private val connectionRequestTimeout: Int,

    @Value("\${integration.gateway.chatgpt.api-key}")
    private val openAiKey: String,
){
    @Bean
    @Qualifier("chatGptRestTemplate")
    fun chatGptRestTemplate(): RestTemplate {
        val gateway = GatewayConfiguration(
            apiKey = openAiKey,
            numberOfConnectionsPool = numberOfConnectionsPool,
            numberOfConnectionsPoolPerRoute = numberOfConnectionsPoolPerRoute,
            socketTimeout = socketTimeout,
            connectionTimeout = connectionTimeout,
            connectionRequestTimeout = connectionRequestTimeout
        )

        return gateway.restTemplate()
    }
}
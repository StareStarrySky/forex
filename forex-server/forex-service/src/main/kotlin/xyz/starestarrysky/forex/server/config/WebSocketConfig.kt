package xyz.starestarrysky.forex.server.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import xyz.starestarrysky.forex.server.property.StompConfig

@Configuration
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @Autowired
    private lateinit var stompConfig: StompConfig

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        stompConfig.endpoints.forEach {
            registry.addEndpoint(it.point).setAllowedOrigins(it.allowedOrigins)
        }
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(stompConfig.broker)
        registry.setApplicationDestinationPrefixes(contextPath)
    }
}

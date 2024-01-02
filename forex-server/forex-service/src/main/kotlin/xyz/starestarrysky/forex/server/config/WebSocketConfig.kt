package xyz.starestarrysky.forex.server.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.scheduling.TaskScheduler
import org.springframework.web.socket.config.WebSocketMessageBrokerStats
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import xyz.starestarrysky.forex.server.property.StompConfig

@Configuration
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    @Value("\${server.servlet.context-path}")
    private lateinit var contextPath: String

    @Autowired
    private lateinit var stompConfig: StompConfig

    @Lazy
    @Autowired
    private lateinit var webSocketStats: WebSocketMessageBrokerStats

    @Lazy
    @Autowired
    private lateinit var messageBrokerTaskScheduler: TaskScheduler

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        stompConfig.endpoints.forEach {
            registry.addEndpoint(it.point).setAllowedOrigins(it.allowedOrigins)
        }
        webSocketStats.loggingPeriod = stompConfig.statsLoggingPeriod
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(stompConfig.broker)
            .setTaskScheduler(messageBrokerTaskScheduler)
//            .setHeartbeatValue(longArrayOf(10000, 10000))
        registry.setApplicationDestinationPrefixes(contextPath)
    }
}

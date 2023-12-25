package xyz.starestarrysky.forex.server.config

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.WebSocketMessageBrokerStats

@Configuration
class SpecialConfig : InitializingBean {
    @Autowired
    private lateinit var webSocketStats: WebSocketMessageBrokerStats

    override fun afterPropertiesSet() {
        webSocketStats()
    }

    fun webSocketStats() {
        webSocketStats.loggingPeriod = 0
    }
}

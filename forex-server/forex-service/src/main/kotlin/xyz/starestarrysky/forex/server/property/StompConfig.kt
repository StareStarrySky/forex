package xyz.starestarrysky.forex.server.property

import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.Serializable

@ConfigurationProperties("push.stomp")
class StompConfig : Serializable {
    companion object {
        private const val serialVersionUID = 2298227968805436544L
    }

    lateinit var broker: String

    lateinit var endpoints: List<StompEndpoint>

    var statsLoggingPeriod: Long = 30 * 60 * 1000

    class StompEndpoint : Serializable {
        companion object {
            private const val serialVersionUID = 248074414613139692L
        }

        lateinit var point: String

        lateinit var allowedOrigins: String
    }
}

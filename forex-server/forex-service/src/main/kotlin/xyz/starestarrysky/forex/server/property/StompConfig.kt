package xyz.starestarrysky.forex.server.property

import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.Serializable

@ConfigurationProperties("push")
class StompConfig : Serializable {
    companion object {
        private const val serialVersionUID = 591606041472408228L
    }

    lateinit var broker: String

    lateinit var endpoints: List<StompEndpoint>

    class StompEndpoint : Serializable {
        companion object {
            private const val serialVersionUID = -4199954564360775922L
        }

        lateinit var point: String

        lateinit var allowedOrigins: String
    }
}

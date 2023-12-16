package xyz.starestarrysky.forex.server.forex.property

import org.springframework.boot.context.properties.ConfigurationProperties
import xyz.starestarrysky.forex.jforex.config.JForexInfo
import java.io.Serializable

@ConfigurationProperties("j-forex")
class JForexConfig : JForexInfo(), Serializable {
    companion object {
        private const val serialVersionUID = -8457782991440119291L
    }
}

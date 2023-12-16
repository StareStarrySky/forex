package xyz.starestarrysky.forex.server.property

import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.Serializable

@ConfigurationProperties("spring.mail")
class EmailConfig : Serializable {
    companion object {
        private const val serialVersionUID = 8172148661630096546L
    }

    lateinit var username: String

    lateinit var to: String
}

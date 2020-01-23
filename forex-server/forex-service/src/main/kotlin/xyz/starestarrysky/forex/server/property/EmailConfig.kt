package xyz.starestarrysky.forex.server.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.mail")
class EmailConfig {
    lateinit var username: String

    lateinit var to: String
}

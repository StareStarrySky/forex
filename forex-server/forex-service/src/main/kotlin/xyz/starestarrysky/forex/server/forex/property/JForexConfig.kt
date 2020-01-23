package xyz.starestarrysky.forex.server.forex.property

import org.springframework.boot.context.properties.ConfigurationProperties
import xyz.starestarrysky.forex.jforex.config.JForexInfo

@ConfigurationProperties("j-forex")
class JForexConfig : JForexInfo()

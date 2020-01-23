package xyz.starestarrysky.forex.server.forex.bean

import com.dukascopy.api.IStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.starestarrysky.forex.jforex.JForexApplication
import xyz.starestarrysky.forex.jforex.JForexApplicationBuilder
import xyz.starestarrysky.forex.server.forex.property.JForexConfig

@Configuration
class ForexApplicationBean {
    @Bean
    fun jForexApplication(jForexConfig: JForexConfig, strategy: IStrategy): JForexApplication {
        return JForexApplicationBuilder.builder().jForexInfo(jForexConfig, strategy).build()
    }
}

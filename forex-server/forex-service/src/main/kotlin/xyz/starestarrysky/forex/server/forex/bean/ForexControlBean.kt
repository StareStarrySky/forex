package xyz.starestarrysky.forex.server.forex.bean

import com.dukascopy.api.IStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.jforex.event.JForexEvent
import xyz.starestarrysky.forex.jforex.platform.JForexPlatform
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiral
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiralIng

@Configuration
class ForexControlBean {
    @Bean
    fun jForexControl(configSettings: MutableList<ConfigSetting>, openOrder: OpenOrder, jForexEvent: JForexEvent): IStrategy {
        return NeverEndingSpiral(JForexPlatform(), openOrder, configSettings, NeverEndingSpiralIng(), jForexEvent)
    }

    @Bean
    fun openOrder(): OpenOrder {
        return OpenOrder()
    }

    @Bean
    fun configSettings(): MutableList<ConfigSetting> {
        return arrayListOf()
    }
}

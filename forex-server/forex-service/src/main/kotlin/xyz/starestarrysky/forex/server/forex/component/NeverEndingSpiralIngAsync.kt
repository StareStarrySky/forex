package xyz.starestarrysky.forex.server.forex.component

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiralIng

@Component
class NeverEndingSpiralIngAsync : NeverEndingSpiralIng() {
    @Async
    override fun onBar(configSetting: ConfigSetting) {
        super.onBar(configSetting)
    }
}

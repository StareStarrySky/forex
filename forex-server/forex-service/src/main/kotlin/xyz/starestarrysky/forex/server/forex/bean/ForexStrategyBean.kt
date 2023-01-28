package xyz.starestarrysky.forex.server.forex.bean

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiralEd
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiralIng

@Configuration
class ForexStrategyBean {
    @Bean
    fun neverEndingSpiralIng(): NeverEndingSpiralEd {
        return NeverEndingSpiralIng()
    }
}

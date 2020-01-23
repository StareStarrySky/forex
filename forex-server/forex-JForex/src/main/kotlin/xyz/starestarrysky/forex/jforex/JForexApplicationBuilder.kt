package xyz.starestarrysky.forex.jforex

import com.dukascopy.api.IStrategy
import xyz.starestarrysky.forex.jforex.config.JForexInfo

class JForexApplicationBuilder {
    private lateinit var jForexInfo: JForexInfo

    private lateinit var strategy: IStrategy

    companion object {
        fun builder(): JForexApplicationBuilder {
            return JForexApplicationBuilder()
        }
    }

    fun jForexInfo(jForexInfo: JForexInfo, strategy: IStrategy): JForexApplicationBuilder {
        return this.apply {
            this.jForexInfo = jForexInfo
            this.strategy = strategy
        }
    }

    fun build(): JForexApplication {
        return JForexApplication(jForexInfo, strategy)
    }
}

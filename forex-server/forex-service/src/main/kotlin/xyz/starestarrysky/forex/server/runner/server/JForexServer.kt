package xyz.starestarrysky.forex.server.runner.server

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import xyz.starestarrysky.forex.jforex.JForexApplication
import xyz.starestarrysky.forex.jforex.JForexTestApplication
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.jforex.platform.JForexPlatform
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiral
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiralIng
import xyz.starestarrysky.forex.server.forex.property.JForexConfig

@Component
class JForexServer : ForexServer {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(JForexServer::class.java)
    }

    @Value("\${spring.config.activate.on-profile}")
    private lateinit var env: String

    @Autowired
    private lateinit var jForexApplication: JForexApplication

    override fun isRunning(): Boolean {
        return if (env == "pro") jForexApplication.isConnected() else false
    }

    @Async
    override fun start() {
        LOGGER.info("开始以${env}环境运行JForexRunner")
        when (env) {
            "pro" -> jForexApplication.run()
            "test" -> runTest()
            else -> LOGGER.info("没有JForexRunner运行")
        }
    }

    private fun runTest() {
        val jForexConfig = JForexConfig()
        val configSettings = arrayListOf<ConfigSetting>()
        JForexTestApplication(jForexConfig, NeverEndingSpiral(JForexPlatform(), OpenOrder(), configSettings, NeverEndingSpiralIng(), null)).run()
    }

    override fun stop() {
        if (env == "pro") {
            LOGGER.info("停止JForexRunner")
            jForexApplication.disconnect()
        }
    }
}

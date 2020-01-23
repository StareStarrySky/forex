package xyz.starestarrysky.forex.server.runner

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import xyz.starestarrysky.forex.jforex.JForexApplication
import xyz.starestarrysky.forex.jforex.JForexTestApplication
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.jforex.platform.JForexPlatform
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiral
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiralIng
import xyz.starestarrysky.forex.server.common.annotation.ForexApplication
import xyz.starestarrysky.forex.server.forex.property.JForexConfig

@ForexApplication(order = 0)
class JForexApplicationRunner : ApplicationRunner {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(JForexApplicationRunner::class.java)
    }

    @Value("\${spring.config.activate.on-profile}")
    private lateinit var env: String

    @Autowired
    private lateinit var jForexApplication: JForexApplication

    override fun run(args: ApplicationArguments?) {
        LOGGER.info("Running JForexApplicationRunner at ${env}...")
        if (StringUtils.equals(env, "pro")) {
            jForexApplication.run()
        } else if (StringUtils.equals(env, "test")) {
            runTest()
        } else {
            LOGGER.info("Nothing is running.")
        }
    }

    private fun runTest() {
        val jForexConfig = JForexConfig()
        val configSettings = arrayListOf<ConfigSetting>()
        JForexTestApplication(jForexConfig, NeverEndingSpiral(JForexPlatform(), OpenOrder(), configSettings, NeverEndingSpiralIng(), null)).run()
    }
}

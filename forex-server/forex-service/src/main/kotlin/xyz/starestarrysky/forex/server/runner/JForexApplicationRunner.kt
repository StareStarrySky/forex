package xyz.starestarrysky.forex.server.runner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import xyz.starestarrysky.forex.server.common.annotation.ForexApplication
import xyz.starestarrysky.forex.server.runner.server.ForexServer

@ForexApplication(order = 0)
class JForexApplicationRunner : ApplicationRunner {
    @Autowired
    private lateinit var jForexServer: ForexServer

    override fun run(args: ApplicationArguments?) {
        jForexServer.start()
    }
}

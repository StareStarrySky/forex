package xyz.starestarrysky.forex.server.schedule

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.starestarrysky.forex.server.common.annotation.DSTScheduled
import xyz.starestarrysky.forex.server.runner.server.ForexServer

@Component
class JForexDSTSchedule : ForexSchedule {
    @Autowired
    private lateinit var server: ForexServer

    @DSTScheduled(cron = "0 5 21 ? * 7")
    fun start() {
        if (!server.isRunning()) {
            server.start()
        }
    }

    @DSTScheduled(cron = "0 55 20 ? * 5")
    fun stop() {
        if (server.isRunning()) {
            server.stop()
        }
    }
}

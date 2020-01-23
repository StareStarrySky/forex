package xyz.starestarrysky.forex.jforex

import com.dukascopy.api.IStrategy
import com.dukascopy.api.Instrument
import com.dukascopy.api.system.ClientFactory
import com.dukascopy.api.system.ISystemListener
import org.slf4j.LoggerFactory
import xyz.starestarrysky.forex.base.ForexRunApplication
import xyz.starestarrysky.forex.jforex.config.JForexInfo
import kotlin.system.exitProcess

class JForexApplication(
    private val jForexInfo: JForexInfo,
    private val strategy: IStrategy
) : ForexRunApplication {
    companion object {
        private const val LOGGER_LINE_PREFIX = "环境日志："
        private val LOGGER = LoggerFactory.getLogger(JForexApplication::class.java)
    }

    private var reconnects = 3

    @Throws(Exception::class)
    override fun run() {
        val client = ClientFactory.getDefaultInstance()

        client.setSystemListener(object : ISystemListener {
            override fun onStart(processId: Long) {
                LOGGER.info("${LOGGER_LINE_PREFIX}Strategy started: $processId")
            }

            override fun onStop(processId: Long) {
                LOGGER.info("${LOGGER_LINE_PREFIX}Strategy stopped: $processId")
                if (client.startedStrategies.isEmpty()) {
                    exitProcess(0)
                }
            }

            override fun onConnect() {
                LOGGER.info("${LOGGER_LINE_PREFIX}Connected")
            }

            override fun onDisconnect() {
                val runnable = Runnable {
                    if (reconnects > 0) {
                        client.reconnect()
                        --reconnects
                    } else {
                        do {
                            try {
                                Thread.sleep(60 * 1000.toLong())
                            } catch (e: InterruptedException) {
                            }
                            try {
                                if (client.isConnected) {
                                    break
                                }
                                client.connect(jForexInfo.uri, jForexInfo.publicKey, jForexInfo.privateKey)
                            } catch (e: Exception) {
                                LOGGER.error(LOGGER_LINE_PREFIX + e.message, e)
                            }
                        } while (!client.isConnected)
                    }
                }
                Thread(runnable).start()
            }
        })

        LOGGER.info("${LOGGER_LINE_PREFIX}Connecting...")
        client.connect(jForexInfo.uri, jForexInfo.publicKey, jForexInfo.privateKey)
        var i = 10
        while (i > 0 && !client.isConnected) {
            Thread.sleep(1000)
            i--
        }
        if (!client.isConnected) {
            LOGGER.error("${LOGGER_LINE_PREFIX}Failed to connect Dukascopy servers")
            exitProcess(1)
        }

        LOGGER.info("${LOGGER_LINE_PREFIX}Subscribing instruments...")
        client.subscribedInstruments = hashSetOf(Instrument.GBPUSD, Instrument.USDJPY, Instrument.GBPJPY)

        LOGGER.info("${LOGGER_LINE_PREFIX}Starting strategy")
        client.startStrategy(strategy)
    }
}

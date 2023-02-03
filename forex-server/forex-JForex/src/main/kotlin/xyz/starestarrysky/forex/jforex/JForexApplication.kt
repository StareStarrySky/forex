package xyz.starestarrysky.forex.jforex

import com.dukascopy.api.IStrategy
import com.dukascopy.api.Instrument
import com.dukascopy.api.system.ClientFactory
import com.dukascopy.api.system.ISystemListener
import org.slf4j.LoggerFactory
import xyz.starestarrysky.forex.base.ForexRunApplication
import xyz.starestarrysky.forex.jforex.config.JForexInfo

class JForexApplication(
    private val jForexInfo: JForexInfo,
    private val strategy: IStrategy
) : ForexRunApplication {
    companion object {
        private const val LOGGER_LINE_PREFIX = "环境日志："
        private val LOGGER = LoggerFactory.getLogger(JForexApplication::class.java)

        private val client = ClientFactory.getDefaultInstance()
    }

    private var reconnects = 3

    private var connect = true

    @Throws(Exception::class)
    override fun run() {
        client.setSystemListener(object : ISystemListener {
            override fun onStart(processId: Long) {
                LOGGER.info("${LOGGER_LINE_PREFIX}环境${processId}已启动")
            }

            override fun onStop(processId: Long) {
                LOGGER.info("${LOGGER_LINE_PREFIX}环境${processId}已停止")
                connect = true
//                if (client.startedStrategies.isEmpty()) {
//                    exitProcess(0)
//                }
            }

            override fun onConnect() {
                LOGGER.info("${LOGGER_LINE_PREFIX}服务器连接成功")
            }

            override fun onDisconnect() {
                if (!connect) {
                    LOGGER.info("${LOGGER_LINE_PREFIX}环境停止中")
                    return
                }
                val runnable = Runnable {
                    if (reconnects > 0) {
                        client.reconnect()
                        --reconnects
                    } else {
                        do {
                            try {
                                Thread.sleep(60 * 1000.toLong())
                            } catch (_: InterruptedException) {
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

        LOGGER.info("${LOGGER_LINE_PREFIX}服务器连接中")
        client.connect(jForexInfo.uri, jForexInfo.publicKey, jForexInfo.privateKey)
        var i = 10
        while (i > 0 && !client.isConnected) {
            Thread.sleep(1000)
            i--
        }
        if (!client.isConnected) {
            LOGGER.error("${LOGGER_LINE_PREFIX}服务器连接失败")
//            exitProcess(1)
            return
        }

        LOGGER.info("${LOGGER_LINE_PREFIX}预置货币对")
        client.subscribedInstruments = hashSetOf(Instrument.GBPUSD, Instrument.USDJPY, Instrument.GBPJPY)

        LOGGER.info("${LOGGER_LINE_PREFIX}开始启动策略")
        client.startStrategy(strategy)
    }

    fun disconnect() {
        if (client.isConnected) {
            connect = false
            client.disconnect()
        }
    }

    fun isConnected(): Boolean {
        return client.isConnected
    }
}

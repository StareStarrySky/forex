package xyz.starestarrysky.forex.jforex

import com.dukascopy.api.IStrategy
import com.dukascopy.api.Instrument
import com.dukascopy.api.JFCurrency
import com.dukascopy.api.LoadingProgressListener
import com.dukascopy.api.system.ISystemListener
import com.dukascopy.api.system.ITesterClient
import com.dukascopy.api.system.TesterFactory
import org.slf4j.LoggerFactory
import xyz.starestarrysky.forex.base.ForexRunApplication
import xyz.starestarrysky.forex.jforex.config.JForexInfo
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException
import kotlin.system.exitProcess

class JForexTestApplication(
    private val jForexInfo: JForexInfo,
    private val strategy: IStrategy
) : ForexRunApplication {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(JForexTestApplication::class.java)
    }

    private val dateFrom = "2019/12/13 01:00:00"
    private val dateTo = "2020/03/03 01:00:00"

    private lateinit var iClient: ITesterClient

    private var lightReconnects: Int = 3
    private val reportsFileLocation = System.getProperty("user.dir") + "/report.html"

    @Throws(Exception::class)
    override fun run() {
        iClient = TesterFactory.getDefaultInstance()

        setSystemListener()
        tryToConnect()
        setDataInterval()
        subscribeToInstruments()
        iClient.setInitialDeposit(JFCurrency.getInstance("USD"), 100.0)
        loadData()

        LOGGER.info("Starting strategy")
        iClient.startStrategy(strategy, getLoadingProgressListener())
    }

    private fun setSystemListener() {
        //set the listener that will receive system events
        iClient.setSystemListener(object : ISystemListener {
            override fun onStart(processId: Long) {
                LOGGER.info("Strategy started: $processId")
            }

            override fun onStop(processId: Long) {
                LOGGER.info("Strategy stopped: $processId")
                val reportFile = File(reportsFileLocation)
                try {
                    iClient.createReport(processId, reportFile)
                } catch (e: Exception) {
                    LOGGER.error(e.message, e)
                }
                if (iClient.startedStrategies.isEmpty()) {
                    exitProcess(0)
                }
            }

            override fun onConnect() {
                LOGGER.info("Connected")
                lightReconnects = 3
            }

            override fun onDisconnect() {
                tryToReconnect()
            }
        })
    }

    @Throws(Exception::class)
    private fun tryToConnect() {
        LOGGER.info("Connecting...")
        //connect to the server using jnlp, user name and password
        //connection is needed for data downloading
        iClient.connect(jForexInfo.uri, jForexInfo.publicKey, jForexInfo.privateKey)
        //wait for it to connect
        var i = 10 //wait max ten seconds
        while (i > 0 && !iClient.isConnected) {
            Thread.sleep(1000)
            i--
        }
        if (!iClient.isConnected) {
            LOGGER.error("Failed to connect Dukascopy servers")
            exitProcess(1)
        }
    }

    private fun tryToReconnect() {
        val runnable = Runnable {
            if (lightReconnects > 0) {
                iClient.reconnect()
                --lightReconnects
            } else {
                do {
                    try {
                        Thread.sleep(60 * 1000.toLong())
                    } catch (e: InterruptedException) {
                    }
                    try {
                        if (iClient.isConnected) {
                            break
                        }
                        iClient.connect(jForexInfo.uri, jForexInfo.publicKey, jForexInfo.privateKey)
                    } catch (e: Exception) {
                        LOGGER.error(e.message, e)
                    }
                } while (!iClient.isConnected)
            }
        }
        Thread(runnable).start()
    }

    @Throws(ParseException::class)
    private fun setDataInterval() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")

        val dateFromObject = dateFormat.parse(dateFrom)
        val dateToObject = dateFormat.parse(dateTo)

        iClient.setDataInterval(ITesterClient.DataLoadingMethod.ALL_TICKS, dateFromObject.time, dateToObject.time)
        LOGGER.info("from: $dateFrom to: $dateTo")
    }

    private fun subscribeToInstruments() {
        val instruments = hashSetOf<Instrument>(Instrument.GBPJPY)
        LOGGER.info("Subscribing instruments...")
        iClient.subscribedInstruments = instruments
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    private fun loadData() {
        //load data
        LOGGER.info("Downloading data")
        val future = iClient.downloadData(null)
        //wait for downloading to complete
        future.get()
    }

    private fun getLoadingProgressListener(): LoadingProgressListener {
        return object : LoadingProgressListener {
            override fun dataLoaded(startTime: Long, endTime: Long, currentTime: Long, information: String) {
                LOGGER.info(information)
            }

            override fun loadingFinished(allDataLoaded: Boolean, startTime: Long, endTime: Long, currentTime: Long) {}

            override fun stopJob(): Boolean {
                return false
            }
        }
    }
}

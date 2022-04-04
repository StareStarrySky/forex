package xyz.starestarrysky.forex.jforex.strategy

import com.dukascopy.api.*
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.jforex.event.JForexEvent
import xyz.starestarrysky.forex.jforex.platform.JForexPlatform
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat

open class NeverEndingSpiralIng : NeverEndingSpiralEd {
    override lateinit var jForexPlatform: JForexPlatform

    override lateinit var openOrder: OpenOrder

    override var jForexEvent: JForexEvent? = null

    override fun onBar(configSetting: ConfigSetting) {
        val history = jForexPlatform.iContext.history

        val bigBIDBar = history.getBar(configSetting.instrument, configSetting.bigPeriod, OfferSide.BID, 0)
        configSetting.bigBIDBarHigh = bigBIDBar.high.toBigDecimal()
        configSetting.bigBIDBarLow = bigBIDBar.low.toBigDecimal()
        configSetting.bigBIDBarOpen = bigBIDBar.open.toBigDecimal()
        configSetting.smallBIDBarOpen = history.getBar(configSetting.instrument, configSetting.smallPeriod, OfferSide.BID, 0).open.toBigDecimal()

        val smallASKBar = history.getBar(configSetting.instrument, configSetting.bigPeriod, OfferSide.ASK, 0)
        configSetting.bigASKBarHigh = smallASKBar.high.toBigDecimal()
        configSetting.bigASKBarLow = smallASKBar.low.toBigDecimal()
        configSetting.bigASKBarOpen = smallASKBar.open.toBigDecimal()
        configSetting.smallASKBarOpen = history.getBar(configSetting.instrument, configSetting.smallPeriod, OfferSide.ASK, 0).open.toBigDecimal()

        passageway(configSetting)

        detail(configSetting)
    }

    private fun passageway(configSetting: ConfigSetting) {
        val smallBIDBarOpen = configSetting.smallBIDBarOpen

        val averages = configSetting.passageways.map { passageway ->
            ((passageway.top + passageway.bottom).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP) - smallBIDBarOpen).abs()
        }
        val minOf = averages.minOf { it }
        val closestPassageway = configSetting.passageways[averages.indexOf(minOf)]

        if (configSetting.curPassageway.top != closestPassageway.bottom && configSetting.curPassageway.bottom != closestPassageway.top) {
            configSetting.curPassageway = closestPassageway
            configSetting.curFuse = 0
        }
    }

    private fun detail(configSetting: ConfigSetting) {
        val bufferPoint = this.round(configSetting.instrument.pipValue * (Math.random() * configSetting.bufferRandom).toInt(), configSetting.instrument)

        val top = configSetting.curPassageway.top
        val bottom = configSetting.curPassageway.bottom

        val topBig = top + bufferPoint
        val topSmall = top - bufferPoint
        val bottomBig = bottom + bufferPoint
        val bottomSmall = bottom - bufferPoint

        if (topSmall < bottomBig) {
            return
        }

        /**
         *      |          -
         *      |      -----|----- -----------
         * -----|-----      |          -
         *      |-     -----|----- -----|-----
         *     -             -           -
         */
        if (((configSetting.bigASKBarOpen < bottomSmall && configSetting.bigASKBarHigh > bottom) || configSetting.bigASKBarOpen > bottomBig) && configSetting.smallASKBarOpen < bottomSmall) {
            val order = openOrder.order[configSetting.instrument.name()]

            if (order == null || order.state != IOrder.State.FILLED) {
                sellAtMarket(configSetting)
                configSetting.curPassageway.run {
                    val tmp = this.top
                    this.top = this.bottom
                    this.bottom = (this.bottom - (tmp - this.bottom))
                }
            } else {
                if (order.isLong) {
                    order.close()
                    sellAtMarket(configSetting)
                    configSetting.curPassageway.run {
                        val tmp = this.top
                        this.top = this.bottom
                        this.bottom = (this.bottom - (tmp - this.bottom))
                    }
                }
            }
        }

        /**
         *      -           -            -
         * ----|------ ----|------     -|
         *     |          -        -----|-----
         * ----|------ -----------      |
         *    -                         |
         */
        if ((configSetting.bigBIDBarOpen < topSmall || (configSetting.bigBIDBarOpen > topBig && configSetting.bigBIDBarLow < top)) && configSetting.smallBIDBarOpen > topBig) {
            val order = openOrder.order[configSetting.instrument.name()]

            if (order == null || order.state != IOrder.State.FILLED) {
                buyAtMarket(configSetting)
                configSetting.curPassageway.run {
                    val tmp = this.bottom
                    this.bottom = this.top
                    this.top = (this.top + (this.top - tmp))
                }
            } else {
                if (!order.isLong) {
                    order.close()
                    buyAtMarket(configSetting)
                    configSetting.curPassageway.run {
                        val tmp = this.bottom
                        this.bottom = this.top
                        this.top = (this.top + (this.top - tmp))
                    }
                }
            }
        }
    }

    private fun buyAtMarket(configSetting: ConfigSetting) {
        tradeAtMarket(configSetting, IEngine.OrderCommand.BUY)
    }

    private fun sellAtMarket(configSetting: ConfigSetting) {
        tradeAtMarket(configSetting, IEngine.OrderCommand.SELL)
    }

    private fun tradeAtMarket(configSetting: ConfigSetting, orderCommand: IEngine.OrderCommand) {
        if (!configSetting.canTrade) {
            return
        }
        if (configSetting.curFuse >= configSetting.fuse) {
            return
        }

        val submitOrder = this.createOrder(configSetting.instrument, orderCommand, configSetting.tradeAmount)
        submitOrder?.run {
            configSetting.curFuse ++
            jForexEvent?.orderCreated(this)
        }
    }

    private fun createOrder(instrument: Instrument, orderCommand: IEngine.OrderCommand, tradeAmount: BigDecimal): IOrder? {
        val label = getLabel()
        return try {
            jForexPlatform.iEngine.submitOrder(label, instrument, orderCommand, tradeAmount.toDouble())
        } finally {
            update()
        }
    }

    private fun getLabel(): String {
        val currentTime = SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())
        return ConfigSetting.LABEL_PREFIX + currentTime + generateRandom() + generateRandom()
    }

    private fun generateRandom(): String {
        var answer = (Math.random() * 10000).toInt().toString()
        if (answer.length > 3) {
            answer = answer.substring(0, 4)
        }
        return answer
    }

    private fun round(price: Double, instrument: Instrument): BigDecimal {
        return price.toBigDecimal().setScale(instrument.pipScale + 1, RoundingMode.HALF_UP)
    }
}

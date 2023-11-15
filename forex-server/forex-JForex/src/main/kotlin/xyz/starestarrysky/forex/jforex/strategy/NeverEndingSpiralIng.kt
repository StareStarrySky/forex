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

    override lateinit var configSetting: ConfigSetting

    override fun onBar() {
        fillConfigSetting()
        passageway()
        detail()
    }

    private fun fillConfigSetting() {
        val history = jForexPlatform.iContext.history

        val bigBIDBar = history.getBar(configSetting.instrument, configSetting.bigPeriod, OfferSide.BID, 0)
        val bigASKBar = history.getBar(configSetting.instrument, configSetting.bigPeriod, OfferSide.ASK, 0)

        configSetting.apply {
            bigBIDBarHigh = bigBIDBar.high.toBigDecimal()
            bigBIDBarLow = bigBIDBar.low.toBigDecimal()
            bigBIDBarOpen = bigBIDBar.open.toBigDecimal()
            smallBIDBarOpen = history.getBar(configSetting.instrument, configSetting.smallPeriod, OfferSide.BID, 0).open.toBigDecimal()

            bigASKBarHigh = bigASKBar.high.toBigDecimal()
            bigASKBarLow = bigASKBar.low.toBigDecimal()
            bigASKBarOpen = bigASKBar.open.toBigDecimal()
            smallASKBarOpen = history.getBar(configSetting.instrument, configSetting.smallPeriod, OfferSide.ASK, 0).open.toBigDecimal()
        }
    }

    private fun passageway() {
        val closestPassagewayNow = findClosestPassageway(configSetting.smallBIDBarOpen)

        val order = openOrder.order[configSetting.instrument.name()]
        configSetting.openPassageway = if (order == null) ConfigSetting.Passageway() else findClosestPassageway(order.openPrice.toBigDecimal())

        if (configSetting.curPassageway.top != closestPassagewayNow.top && configSetting.curPassageway.bottom != closestPassagewayNow.bottom
            && configSetting.curPassageway.top != closestPassagewayNow.bottom && configSetting.curPassageway.bottom != closestPassagewayNow.top) {
            configSetting.curPassageway.run {
                this.top = closestPassagewayNow.top
                this.bottom = closestPassagewayNow.bottom
            }
        }
    }

    private fun findClosestPassageway(price: BigDecimal): ConfigSetting.Passageway {
        val averages = configSetting.passageways.map { passageway ->
            ((passageway.top + passageway.bottom).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP) - price).abs()
        }
        val minOf = averages.minOf { it }
        return configSetting.passageways[averages.indexOf(minOf)]
    }

    private fun detail() {
        val bufferPoint = round(configSetting.instrument.pipValue * (Math.random() * configSetting.bufferRandom).toInt(), configSetting.instrument)

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
                val hasTraded = sellAtMarket(null)
                if (hasTraded) {
                    configSetting.curPassageway.run {
                        val tmp = this.top
                        this.top = this.bottom
                        this.bottom = (this.bottom - (tmp - this.bottom))
                    }
                }
            } else {
                if (order.isLong) {
                    val hasTraded = sellAtMarket(order)
                    if (hasTraded) {
                        configSetting.curPassageway.run {
                            val tmp = this.top
                            this.top = this.bottom
                            this.bottom = (this.bottom - (tmp - this.bottom))
                        }
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
                val hasTraded = buyAtMarket(null)
                if (hasTraded) {
                    configSetting.curPassageway.run {
                        val tmp = this.bottom
                        this.bottom = this.top
                        this.top = (this.top + (this.top - tmp))
                    }
                }
            } else {
                if (!order.isLong) {
                    val hasTraded = buyAtMarket(order)
                    if (hasTraded) {
                        configSetting.curPassageway.run {
                            val tmp = this.bottom
                            this.bottom = this.top
                            this.top = (this.top + (this.top - tmp))
                        }
                    }
                }
            }
        }
    }

    private fun buyAtMarket(order: IOrder?): Boolean {
        return tradeAtMarket(IEngine.OrderCommand.BUY, order)
    }

    private fun sellAtMarket(order: IOrder?): Boolean {
        return tradeAtMarket(IEngine.OrderCommand.SELL, order)
    }

    private fun tradeAtMarket(orderCommand: IEngine.OrderCommand, order: IOrder?): Boolean {
        if (!configSetting.canTrade) {
            return false
        }

        closeOrder(order)

        val checkTrade = checkFuse()
        if (!checkTrade) {
            configSetting.curFuse = 0
            return false
        }

        val submitOrder = createOrderMain(configSetting.instrument, orderCommand, configSetting.tradeAmount)
        submitOrder?.run {
            configSetting.curFuse ++
            jForexEvent?.orderCreated(this)
        }
        return true
    }

    private fun checkFuse(): Boolean {
        if (configSetting.curPassageway.top != configSetting.openPassageway.top && configSetting.curPassageway.bottom != configSetting.openPassageway.bottom
            && configSetting.curPassageway.top != configSetting.openPassageway.bottom && configSetting.curPassageway.bottom != configSetting.openPassageway.top) {
            configSetting.curFuse = 0
        }
        return configSetting.curFuse < configSetting.fuse
    }

    private fun closeOrder(order: IOrder?) {
        try {
            order?.close()
        } finally {
            update()
        }
    }

    private fun createOrderMain(instrument: Instrument, orderCommand: IEngine.OrderCommand, tradeAmount: BigDecimal): IOrder? {
        return if (orderCommand == IEngine.OrderCommand.BUY) {
            val curIndex = configSetting.passageways.indexOfFirst { it.top == configSetting.curPassageway.bottom || it.bottom == configSetting.curPassageway.bottom }
            val stopLossVal = configSetting.stopLossPip.toBigDecimal() * configSetting.instrument.pipValue.toBigDecimal() + (configSetting.curPassageway.top - configSetting.curPassageway.bottom)
            if (curIndex == configSetting.passageways.size - 1 || (curIndex < configSetting.passageways.size - 1 && configSetting.curPassageway.bottom - configSetting.passageways[curIndex + 1].top > stopLossVal)) {
                createOrder(instrument, orderCommand, tradeAmount, configSetting.curPassageway.bottom - stopLossVal)
            } else {
                createOrder(instrument, orderCommand, tradeAmount)
            }
        } else {
            val curIndex = configSetting.passageways.indexOfFirst { it.top == configSetting.curPassageway.top || it.bottom == configSetting.curPassageway.top }
            val stopLossVal = configSetting.stopLossPip.toBigDecimal() * configSetting.instrument.pipValue.toBigDecimal() + (configSetting.curPassageway.top - configSetting.curPassageway.bottom)
            if (curIndex == 0 || (curIndex > 0 && configSetting.passageways[curIndex - 1].bottom - configSetting.curPassageway.top > stopLossVal)) {
                createOrder(instrument, orderCommand, tradeAmount, configSetting.curPassageway.top + stopLossVal)
            } else {
                createOrder(instrument, orderCommand, tradeAmount)
            }
        }
    }

    private fun createOrder(instrument: Instrument, orderCommand: IEngine.OrderCommand, tradeAmount: BigDecimal): IOrder? {
        return createOrder(instrument, orderCommand, tradeAmount, BigDecimal.ZERO)
    }

    private fun createOrder(instrument: Instrument, orderCommand: IEngine.OrderCommand, tradeAmount: BigDecimal, stopLossPrice: BigDecimal): IOrder? {
        val label = getLabel()
        return try {
            jForexPlatform.iEngine.submitOrder(label, instrument, orderCommand, tradeAmount.toDouble(), 0.0, -1.0, stopLossPrice.toDouble(), 0.0)
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

    override fun closeOrder(id: String) {
        closeOrder(openOrder.all.find { iOrder -> iOrder.id == id } ?: return)
    }

    override fun changeOrderCommand(id: String) {
        val order = openOrder.all.find { iOrder -> iOrder.id == id } ?: return

        val instrument = order.instrument
        val orderCommand = if (order.isLong) IEngine.OrderCommand.SELL else IEngine.OrderCommand.BUY
        val amount = order.amount.toBigDecimal()

        closeOrder(order)
        createOrder(instrument, orderCommand, amount)
    }
}

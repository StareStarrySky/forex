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
        configSetting.openPassageway = if (order == null) BigDecimal.ZERO else findClosestPassageway(order.openPrice.toBigDecimal())

        if (configSetting.curPassageway.compareTo(closestPassagewayNow) != 0) {
            configSetting.curPassageway = closestPassagewayNow
        }
    }

    private fun findClosestPassageway(price: BigDecimal): BigDecimal {
        val averages = configSetting.passageways.map { passageway -> (passageway - price).abs() }
        val minOf = averages.minOf { it }
        return configSetting.passageways[averages.indexOf(minOf)]
    }

    private fun detail() {
        val bufferPoint = round(configSetting.instrument.pipValue * (Math.random() * configSetting.bufferRandom).toInt(), configSetting.instrument)

        val passageway = configSetting.curPassageway

        val passagewayBig = passageway + bufferPoint
        val passagewaySmall = passageway - bufferPoint

        /**
         *      |
         * -----|-----
         *      |-
         */
        if (configSetting.bigASKBarHigh > passageway && configSetting.smallASKBarOpen < passagewaySmall) {
            val order = openOrder.order[configSetting.instrument.name()]

            if (order == null || order.state != IOrder.State.FILLED) {
                sellAtMarket(null)
            } else {
                if (order.isLong) {
                    sellAtMarket(order)
                }
            }
        }

        /**
         *      |-
         * -----|-----
         *      |
         */
        if (configSetting.bigBIDBarLow < passageway && configSetting.smallBIDBarOpen > passagewayBig) {
            val order = openOrder.order[configSetting.instrument.name()]

            if (order == null || order.state != IOrder.State.FILLED) {
                buyAtMarket(null)
            } else {
                if (order.isLong) {
                    buyAtMarket(order)
                }
            }
        }
    }

    private fun buyAtMarket(order: IOrder?) {
        tradeAtMarket(IEngine.OrderCommand.BUY, order)
    }

    private fun sellAtMarket(order: IOrder?) {
        tradeAtMarket(IEngine.OrderCommand.SELL, order)
    }

    private fun tradeAtMarket(orderCommand: IEngine.OrderCommand, order: IOrder?) {
        if (!configSetting.canTrade) {
            return
        }

        closeOrder(order)

        val checkFuse = checkFuse()
        if (!checkFuse) {
            configSetting.curFuse = 0
            jForexEvent?.fused()
            return
        }

        val submitOrder = createOrderMain(orderCommand)
        submitOrder?.run {
            configSetting.curFuse ++
            jForexEvent?.orderCreated(this)
        }
    }

    private fun checkFuse(): Boolean {
        if (configSetting.curPassageway.compareTo(configSetting.openPassageway) != 0) {
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

    private fun createOrderMain(orderCommand: IEngine.OrderCommand): IOrder? {
        val curIndex = configSetting.passageways.indexOfFirst { it.compareTo(configSetting.curPassageway) == 0 }
        val stopLossVal = getStopLossVal(configSetting)
        return if (orderCommand == IEngine.OrderCommand.BUY) {
            if (curIndex == configSetting.passageways.size - 1 || (curIndex < configSetting.passageways.size - 1 && configSetting.curPassageway - configSetting.passageways[curIndex + 1] > stopLossVal)) {
                createOrder(configSetting.instrument, orderCommand, configSetting.tradeAmount, configSetting.curPassageway - stopLossVal)
            } else {
                createOrder(configSetting.instrument, orderCommand, configSetting.tradeAmount)
            }
        } else {
            if (curIndex == 0 || (curIndex > 0 && configSetting.passageways[curIndex - 1] - configSetting.curPassageway > stopLossVal)) {
                createOrder(configSetting.instrument, orderCommand, configSetting.tradeAmount, configSetting.curPassageway + stopLossVal)
            } else {
                createOrder(configSetting.instrument, orderCommand, configSetting.tradeAmount)
            }
        }
    }

    private fun getStopLossVal(config: ConfigSetting): BigDecimal {
        return config.stopLossPip.toBigDecimal() * config.instrument.pipValue.toBigDecimal()
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

    // TODO: use bookstore library
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

    override fun createOrderModel(configSettings: MutableList<ConfigSetting>, instrument: Instrument, orderCommand: IEngine.OrderCommand) {
        openOrder.order[instrument.name()] ?: return

        val config = configSettings.find { configSetting -> configSetting.instrument == instrument } ?: return

        val stopLossVal = getStopLossVal(config)
        createOrder(instrument, orderCommand, config.tradeAmount, if (orderCommand == IEngine.OrderCommand.BUY) config.curPassageway - stopLossVal else config.curPassageway + stopLossVal)
    }
}

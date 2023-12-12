package xyz.starestarrysky.forex.jforex.strategy

import com.dukascopy.api.IEngine
import com.dukascopy.api.IOrder
import com.dukascopy.api.Instrument
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.jforex.event.JForexEvent
import xyz.starestarrysky.forex.jforex.platform.JForexPlatform

interface NeverEndingSpiralEd {
    var jForexPlatform: JForexPlatform

    var openOrder: OpenOrder

    var jForexEvent: JForexEvent?

    var configSetting: ConfigSetting

    fun init(jForexPlatform: JForexPlatform, openOrder: OpenOrder, jForexEvent: JForexEvent?) {
        this.jForexPlatform = jForexPlatform
        this.openOrder = openOrder
        this.jForexEvent = jForexEvent
        update()
    }

    fun update() {
        openOrder.all = jForexPlatform.iEngine.orders.filter { it.state == IOrder.State.FILLED }
        openOrder.orders = openOrder.all.filter { !it.label.startsWith(ConfigSetting.LABEL_PREFIX) }
        openOrder.order = openOrder.all.filter { it.label.startsWith(ConfigSetting.LABEL_PREFIX) }.groupBy { it.instrument.name() }.mapValues { it.value.first() }
    }

    fun onBar()

    fun closeOrder(id: String)

    fun changeOrderCommand(id: String)

    fun createOrderModel(configSettings: MutableList<ConfigSetting>, instrument: Instrument, orderCommand: IEngine.OrderCommand)
}

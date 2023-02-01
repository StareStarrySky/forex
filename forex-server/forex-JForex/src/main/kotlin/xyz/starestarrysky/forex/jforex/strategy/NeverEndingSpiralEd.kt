package xyz.starestarrysky.forex.jforex.strategy

import com.dukascopy.api.IOrder
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.jforex.event.JForexEvent
import xyz.starestarrysky.forex.jforex.platform.JForexPlatform

interface NeverEndingSpiralEd {
    var jForexPlatform: JForexPlatform

    var openOrder: OpenOrder

    var jForexEvent: JForexEvent?

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

    fun onBar(configSetting: ConfigSetting)

    fun closeOrder(id: String)

    fun changeOrderCommand(id: String)
}

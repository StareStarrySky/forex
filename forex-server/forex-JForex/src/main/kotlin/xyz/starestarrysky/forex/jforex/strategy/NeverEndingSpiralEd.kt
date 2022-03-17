package xyz.starestarrysky.forex.jforex.strategy

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
        openOrder.orders = jForexPlatform.iEngine.orders
        openOrder.order = openOrder.orders.groupBy { it.instrument.name() }.mapValues { it.value.first { iOrder -> iOrder.label.startsWith(ConfigSetting.LABEL_PREFIX) && it.key == iOrder.instrument.name() } }
    }

    fun onBar(configSetting: ConfigSetting)
}

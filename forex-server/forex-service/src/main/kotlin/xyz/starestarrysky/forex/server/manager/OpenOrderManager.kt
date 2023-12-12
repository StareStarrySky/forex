package xyz.starestarrysky.forex.server.manager

import com.dukascopy.api.IEngine
import com.dukascopy.api.Instrument
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.server.forex.model.OpenOrderModel

interface OpenOrderManager {
    var openOrder: OpenOrder

    fun returnOrder(): OpenOrder {
        return OpenOrderModel().apply {
            this.orders = openOrder.orders
            this.order = openOrder.order
        }
    }

    fun getOrders(): OpenOrder

    fun closeOrder(id: String): OpenOrder

    fun changeOrderCommand(id: String): OpenOrder

    fun createOrder(instrument: Instrument, orderCommand: IEngine.OrderCommand): OpenOrder
}

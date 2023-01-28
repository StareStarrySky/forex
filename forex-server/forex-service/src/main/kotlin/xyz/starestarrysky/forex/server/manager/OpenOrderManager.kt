package xyz.starestarrysky.forex.server.manager

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
}

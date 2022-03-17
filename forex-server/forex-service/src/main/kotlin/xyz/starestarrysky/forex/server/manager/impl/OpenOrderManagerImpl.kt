package xyz.starestarrysky.forex.server.manager.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.server.forex.model.OpenOrderModel
import xyz.starestarrysky.forex.server.manager.OpenOrderManager

@Service
class OpenOrderManagerImpl : OpenOrderManager {
    @Autowired
    private lateinit var openOrder: OpenOrder

    override fun getOpenOrder(): OpenOrder {
        return OpenOrderModel().apply {
            this.orders = openOrder.orders
            this.order = openOrder.order
        }
    }
}

package xyz.starestarrysky.forex.server.manager.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.server.manager.OpenOrderManager

@Service
class OpenOrderManagerImpl : OpenOrderManager {
    @Autowired
    override lateinit var openOrder: OpenOrder

    override fun getOrders(): OpenOrder {
        return returnOrder()
    }

    override fun closeOrder(id: String): OpenOrder {
        openOrder.orderIdToClose = id
        return returnOrder()
    }

    override fun changeOrderCommand(id: String): OpenOrder {
        openOrder.orderIdToChange = id
        return returnOrder()
    }
}

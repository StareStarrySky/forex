package xyz.starestarrysky.forex.server.manager.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.jforex.strategy.NeverEndingSpiralEd
import xyz.starestarrysky.forex.server.manager.OpenOrderManager

@Service
class OpenOrderManagerImpl : OpenOrderManager {
    @Autowired
    override lateinit var openOrder: OpenOrder

    @Autowired
    private lateinit var neverEndingSpiralEd: NeverEndingSpiralEd

    override fun getOrders(): OpenOrder {
        return returnOrder()
    }

    override fun closeOrder(id: String): OpenOrder {
        neverEndingSpiralEd.closeOrder(id)
        return returnOrder()
    }

    override fun changeOrderCommand(id: String): OpenOrder {
        neverEndingSpiralEd.changeOrderCommand(id)
        return returnOrder()
    }
}

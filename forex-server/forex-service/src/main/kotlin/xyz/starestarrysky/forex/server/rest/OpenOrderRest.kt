package xyz.starestarrysky.forex.server.rest

import com.dukascopy.api.IEngine
import com.dukascopy.api.Instrument
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.server.common.base.BaseRest
import xyz.starestarrysky.forex.server.forex.model.OpenOrderModel
import xyz.starestarrysky.forex.server.manager.OpenOrderManager

@RestController
@RequestMapping("/open-order")
class OpenOrderRest : BaseRest() {
    @Autowired
    private lateinit var openOrderManager: OpenOrderManager

    @GetMapping
    @JsonView(OpenOrderModel.ModelView::class)
    fun getOrders(): OpenOrder {
        return openOrderManager.getOrders()
    }

    @DeleteMapping("/{id}")
    @JsonView(OpenOrderModel.ModelView::class)
    fun closeOrder(@PathVariable("id") id: String): OpenOrder {
        return openOrderManager.closeOrder(id)
    }

    @PutMapping("/{id}/order-command")
    @JsonView(OpenOrderModel.ModelView::class)
    fun changeOrderCommand(@PathVariable("id") id: String): OpenOrder {
        return openOrderManager.changeOrderCommand(id)
    }

    @PostMapping("/{instrument}/{order-command}")
    @JsonView(OpenOrderModel.ModelView::class)
    fun createOrder(@PathVariable("instrument") instrument: Instrument, @PathVariable("order-command") orderCommand: IEngine.OrderCommand): OpenOrder {
        return openOrderManager.createOrder(instrument, orderCommand)
    }
}

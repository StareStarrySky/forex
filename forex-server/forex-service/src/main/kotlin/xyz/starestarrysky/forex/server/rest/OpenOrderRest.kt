package xyz.starestarrysky.forex.server.rest

import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    fun getOpenOder(): OpenOrder {
        return openOrderManager.getOpenOrder()
    }
}

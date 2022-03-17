package xyz.starestarrysky.forex.server.forex.model

import com.dukascopy.api.IOrder
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import xyz.starestarrysky.forex.jforex.entity.OpenOrder
import xyz.starestarrysky.forex.server.forex.model.serialize.IOrderSerializer
import java.io.Serializable

class OpenOrderModel : OpenOrder(), Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    interface ModelView

    @JsonView(ModelView::class)
    @JsonSerialize(contentUsing = IOrderSerializer::class)
    override var order: Map<String, IOrder> = hashMapOf()

    @JsonView(ModelView::class)
    @JsonSerialize(contentUsing = IOrderSerializer::class)
    override var orders: List<IOrder> = arrayListOf()
}

package xyz.starestarrysky.forex.jforex.entity

import com.dukascopy.api.IEngine
import com.dukascopy.api.IOrder
import com.dukascopy.api.Instrument

open class OpenOrder {
    open lateinit var order: Map<String, IOrder>

    open lateinit var orders: List<IOrder>

    lateinit var all: List<IOrder>

    var orderIdToClose: String? = null

    var orderIdToChange: String? = null

    var instrument4Create: Instrument? = null

    var orderCommand4Create: IEngine.OrderCommand? = null
}

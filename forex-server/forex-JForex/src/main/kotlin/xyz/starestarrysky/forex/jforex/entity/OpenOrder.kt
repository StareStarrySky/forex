package xyz.starestarrysky.forex.jforex.entity

import com.dukascopy.api.IOrder

open class OpenOrder {
    open lateinit var order: Map<String, IOrder>

    open lateinit var orders: List<IOrder>

    lateinit var all: List<IOrder>
}

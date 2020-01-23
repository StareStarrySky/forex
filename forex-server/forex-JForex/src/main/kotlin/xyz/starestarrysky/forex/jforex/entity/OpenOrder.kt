package xyz.starestarrysky.forex.jforex.entity

import com.dukascopy.api.IOrder

class OpenOrder {
    lateinit var order: Map<String, IOrder>

    lateinit var orders: Map<String, List<IOrder>>
}

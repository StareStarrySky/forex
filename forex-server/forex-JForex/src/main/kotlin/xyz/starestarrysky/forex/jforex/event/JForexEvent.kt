package xyz.starestarrysky.forex.jforex.event

import com.dukascopy.api.IOrder

interface JForexEvent {
    fun orderCreated(order: IOrder)

    fun fused()
}

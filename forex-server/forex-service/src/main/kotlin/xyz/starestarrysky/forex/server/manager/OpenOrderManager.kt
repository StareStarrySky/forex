package xyz.starestarrysky.forex.server.manager

import xyz.starestarrysky.forex.jforex.entity.OpenOrder

interface OpenOrderManager {
    fun getOpenOrder(): OpenOrder
}

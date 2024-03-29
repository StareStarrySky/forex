package xyz.starestarrysky.forex.jforex.entity

import com.dukascopy.api.Instrument
import com.dukascopy.api.Period
import java.math.BigDecimal

open class ConfigSetting {
    companion object {
        const val LABEL_PREFIX = "ZXY_IVF"
    }

    open var canTrade = true

    open var fuse = 0

    open var curFuse = 0

    open var bufferRandom = 0

    var stopLossPip = 100

    open lateinit var bigPeriod: Period

    open lateinit var smallPeriod: Period

    open lateinit var instrument: Instrument

    open lateinit var tradeAmount: BigDecimal

    open lateinit var passageways: List<BigDecimal>

    open lateinit var curPassageway: BigDecimal

    var openPassageway: BigDecimal = BigDecimal.ZERO

    lateinit var bigBIDBarHigh: BigDecimal

    lateinit var bigBIDBarLow: BigDecimal

    lateinit var bigBIDBarOpen: BigDecimal

    lateinit var smallBIDBarOpen: BigDecimal

    lateinit var bigASKBarHigh: BigDecimal

    lateinit var bigASKBarLow: BigDecimal

    lateinit var bigASKBarOpen: BigDecimal

    lateinit var smallASKBarOpen: BigDecimal
}

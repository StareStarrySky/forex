package xyz.starestarrysky.forex.server.forex.model

import com.dukascopy.api.Instrument
import com.dukascopy.api.Period
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.server.forex.model.deserialize.InstrumentDeserializer
import xyz.starestarrysky.forex.server.forex.model.serialize.InstrumentSerializer
import java.io.Serializable
import java.math.BigDecimal

class ConfigSettingModel : ConfigSetting(), Serializable {
    companion object {
        private const val serialVersionUID = 7780827240446251804L
    }

    interface ModelView

    @JsonView(ModelView::class)
    override var canTrade = true

    @JsonView(ModelView::class)
    override var fuse = 4

    @JsonView(ModelView::class)
    override var curFuse = 0

    @JsonView(ModelView::class)
    override var bufferRandom = 0

    override var bigPeriod: Period = Period.FOUR_HOURS

    override var smallPeriod: Period = Period.FIVE_MINS

    @JsonView(ModelView::class)
    @JsonSerialize(using = InstrumentSerializer::class)
    @JsonDeserialize(using = InstrumentDeserializer::class)
    override var instrument: Instrument = Instrument.GBPJPY

    @JsonView(ModelView::class)
    override var tradeAmount: BigDecimal = BigDecimal.ONE

    @JsonView(ModelView::class)
    override var passageways: List<BigDecimal> = arrayListOf()

    @JsonView(ModelView::class)
    override var curPassageway: BigDecimal = BigDecimal.ZERO
}

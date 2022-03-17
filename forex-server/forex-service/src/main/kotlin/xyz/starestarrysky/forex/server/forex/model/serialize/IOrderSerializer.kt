package xyz.starestarrysky.forex.server.forex.model.serialize

import com.dukascopy.api.IOrder
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class IOrderSerializer : JsonSerializer<IOrder>() {
    override fun serialize(value: IOrder?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.run {
            writeStartObject(value, 9)
            writeObjectField("id", value?.id)
            writeObjectField("label", value?.label)
            writeObjectField("fillTime", value?.fillTime)
            writeObjectField("instrument", value?.instrument?.name)
            writeObjectField("orderCommand", value?.orderCommand?.name)
            writeObjectField("openPrice", value?.openPrice)
            writeObjectField("originalAmount", value?.originalAmount)
            writeObjectField("profitLossInPips", value?.profitLossInPips)
            writeObjectField("profitLossInAccountCurrency", value?.profitLossInAccountCurrency)
            writeEndObject()
        }
    }
}

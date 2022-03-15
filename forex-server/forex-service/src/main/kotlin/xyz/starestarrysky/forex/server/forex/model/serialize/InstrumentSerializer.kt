package xyz.starestarrysky.forex.server.forex.model.serialize

import com.dukascopy.api.Instrument
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class InstrumentSerializer : JsonSerializer<Instrument>() {
    override fun serialize(value: Instrument?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.run {
            this.writeStartObject(value, 2)
            this.writeObjectField("name", value?.name())
            this.writeObjectField("stringValue", value?.name)
            this.writeEndObject()
        }
    }
}

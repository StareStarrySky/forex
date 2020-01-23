package xyz.starestarrysky.forex.server.forex.model.serialize

import com.dukascopy.api.Instrument
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class InstrumentSerializer : JsonSerializer<Instrument>() {
    override fun serialize(p0: Instrument?, p1: JsonGenerator?, p2: SerializerProvider?) {
        p1?.run {
            this.writeStartObject(p0, 2)
            this.writeObjectField("name", p0?.name())
            this.writeObjectField("stringValue", p0?.name)
            this.writeEndObject()
        }
    }
}

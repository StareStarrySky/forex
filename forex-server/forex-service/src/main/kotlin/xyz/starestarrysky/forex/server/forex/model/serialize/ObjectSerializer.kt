package xyz.starestarrysky.forex.server.forex.model.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting

@JsonComponent
class ObjectSerializer : JsonSerializer<Any>() {
    override fun serialize(value: Any?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeObject(value)
    }
}

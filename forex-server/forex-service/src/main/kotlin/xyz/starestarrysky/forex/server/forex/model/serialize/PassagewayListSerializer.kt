package xyz.starestarrysky.forex.server.forex.model.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting

@JsonComponent
class PassagewayListSerializer : JsonSerializer<List<ConfigSetting.Passageway>>() {
    override fun serialize(value: List<ConfigSetting.Passageway>?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeObject(value)
    }
}

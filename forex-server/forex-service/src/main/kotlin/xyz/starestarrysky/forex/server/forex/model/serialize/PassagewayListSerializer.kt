package xyz.starestarrysky.forex.server.forex.model.serialize

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting

@JsonComponent
class PassagewayListSerializer : JsonSerializer<List<ConfigSetting.Passageway>>() {
    override fun serialize(p0: List<ConfigSetting.Passageway>?, p1: JsonGenerator?, p2: SerializerProvider?) {
        p1?.writeObject(p0)
    }
}

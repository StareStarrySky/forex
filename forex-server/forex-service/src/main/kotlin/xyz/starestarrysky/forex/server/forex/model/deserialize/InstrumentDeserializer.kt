package xyz.starestarrysky.forex.server.forex.model.deserialize

import com.dukascopy.api.Instrument
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.TextNode
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class InstrumentDeserializer : JsonDeserializer<Instrument>() {
    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): Instrument? {
        return Instrument.valueOf((p0?.codec?.readTree<TreeNode>(p0)?.get("name") as TextNode).asText())
    }
}

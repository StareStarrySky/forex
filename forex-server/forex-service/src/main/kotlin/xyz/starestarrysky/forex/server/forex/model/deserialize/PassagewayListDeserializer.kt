package xyz.starestarrysky.forex.server.forex.model.deserialize

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.ArrayNode
import org.springframework.boot.jackson.JsonComponent
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import java.math.BigDecimal

@JsonComponent
class PassagewayListDeserializer : JsonDeserializer<List<ConfigSetting.Passageway>>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): List<ConfigSetting.Passageway>? {
        return (p?.codec?.readTree<TreeNode>(p) as ArrayNode).map {
            ConfigSetting.Passageway().apply {
                this.top = BigDecimal(it["top"].asText())
                this.bottom = BigDecimal(it["bottom"].asText())
            }
        }
    }
}

package xyz.starestarrysky.forex.server.rest

import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.server.common.base.BaseRest
import xyz.starestarrysky.forex.server.forex.model.ConfigSettingModel
import xyz.starestarrysky.forex.server.manager.ConfigSettingManager

@RestController
@RequestMapping("/config-setting")
class ConfigSettingRest : BaseRest() {
    @Autowired
    private lateinit var configSettingManager: ConfigSettingManager

    @GetMapping
    @JsonView(ConfigSettingModel.ModelView::class)
    fun getConfigSetting(): MutableList<ConfigSetting> {
        return configSettingManager.getConfigSettings()
    }

    @PutMapping
    @JsonView(ConfigSettingModel.ModelView::class)
    fun updateConfigSetting(@RequestBody configSettings: List<ConfigSettingModel>): MutableList<ConfigSetting> {
        return configSettingManager.putConfigSettings(configSettings)
    }
}

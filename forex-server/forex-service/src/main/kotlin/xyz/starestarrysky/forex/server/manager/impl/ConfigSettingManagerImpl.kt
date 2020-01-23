package xyz.starestarrysky.forex.server.manager.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import xyz.starestarrysky.forex.jforex.entity.ConfigSetting
import xyz.starestarrysky.forex.server.manager.ConfigSettingManager

@Service
@CacheConfig(cacheNames = ["configSettingManager"])
class ConfigSettingManagerImpl : ConfigSettingManager {
    @Autowired
    private lateinit var configSettings: List<ConfigSetting>

    @Cacheable(key = "'configSettings'")
    override fun getConfigSettings(): List<ConfigSetting> {
        return configSettings
    }

    @CachePut(key = "'configSettings'")
    override fun putConfigSettings(configSettings: List<ConfigSetting>): List<ConfigSetting> {
        this.configSettings = configSettings
        return this.configSettings
    }
}

package xyz.starestarrysky.forex.server.manager

import xyz.starestarrysky.forex.jforex.entity.ConfigSetting

interface ConfigSettingManager {
    fun getConfigSettings(): List<ConfigSetting>

    fun putConfigSettings(configSettings: List<ConfigSetting>): List<ConfigSetting>
}

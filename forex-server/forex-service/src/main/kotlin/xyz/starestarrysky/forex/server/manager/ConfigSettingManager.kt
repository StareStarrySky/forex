package xyz.starestarrysky.forex.server.manager

import xyz.starestarrysky.forex.jforex.entity.ConfigSetting

interface ConfigSettingManager {
    fun getConfigSettings(): MutableList<ConfigSetting>

    fun putConfigSettings(configSettingList: List<ConfigSetting>): MutableList<ConfigSetting>
}

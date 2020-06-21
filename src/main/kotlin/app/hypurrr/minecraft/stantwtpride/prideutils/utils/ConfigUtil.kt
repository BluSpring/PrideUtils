package app.hypurrr.minecraft.stantwtpride.prideutils.utils

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import org.bukkit.configuration.file.FileConfiguration


object ConfigUtil {
    fun loadConfig() {
        Init.plugin.config.options().copyHeader(true)
        Init.plugin.saveConfig()
    }
}
package app.hypurrr.minecraft.stantwtpride.prideutils

import app.hypurrr.minecraft.stantwtpride.prideutils.commands.HideCommand
import app.hypurrr.minecraft.stantwtpride.prideutils.commands.PlayCommand
import app.hypurrr.minecraft.stantwtpride.prideutils.commands.ShowCommand
import app.hypurrr.minecraft.stantwtpride.prideutils.listeners.InventoryClickListener
import app.hypurrr.minecraft.stantwtpride.prideutils.listeners.InventoryCloseListener
import app.hypurrr.minecraft.stantwtpride.prideutils.listeners.PlayerOnJoinListener
import app.hypurrr.minecraft.stantwtpride.prideutils.listeners.PlayerPreLoginListener
import app.hypurrr.minecraft.stantwtpride.prideutils.player.MusicPlayer
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.ConfigUtil
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.HidePlayersStorageUtil
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.types.IStorage
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Init : JavaPlugin() {
    private val log = this.server.logger

    override fun onEnable() {
        plugin = this
        music = MusicPlayer()
        ConfigUtil.loadConfig()
        HidePlayersStorageUtil.loadStorage()
        log.info("PrideUtils is now enabled!")

        this.getCommand("play")?.setExecutor(PlayCommand)
        this.getCommand("hide")?.setExecutor(HideCommand)
        this.getCommand("show")?.setExecutor(ShowCommand)
        this.server.pluginManager.registerEvents(InventoryClickListener, this)
        this.server.pluginManager.registerEvents(InventoryCloseListener, this)
        this.server.pluginManager.registerEvents(PlayerOnJoinListener, this)
    }

    override fun onDisable() {
        log.info("PrideUtils has been disabled.")
        if (music.currentlyPlaying != null) {
            music.stopPlayer()
        }
    }

    companion object {
        lateinit var plugin: JavaPlugin
        lateinit var music: MusicPlayer
    }
}
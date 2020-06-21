package app.hypurrr.minecraft.stantwtpride.prideutils.listeners

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import app.hypurrr.minecraft.stantwtpride.prideutils.commands.PlayCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

object InventoryCloseListener : Listener {
    @EventHandler
    fun onInvClose(e: InventoryCloseEvent) {
        try {
            if (e.player.name == PlayCommand.player!!.name) {
                Init.plugin.server.logger.info("${e.player.name} closed inventory.")
                PlayCommand.player = null
                PlayCommand.gui = null
            }
        } catch (_: Exception) {}
    }
}
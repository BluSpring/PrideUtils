package app.hypurrr.minecraft.stantwtpride.prideutils.listeners

import app.hypurrr.minecraft.stantwtpride.prideutils.commands.PlayCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

object InventoryClickListener : Listener {
    @EventHandler
    fun onInvClick(e: InventoryClickEvent) {
        PlayCommand.onClick(e)
    }
}
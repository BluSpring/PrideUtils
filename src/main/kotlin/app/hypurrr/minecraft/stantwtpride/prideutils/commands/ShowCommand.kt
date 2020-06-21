package app.hypurrr.minecraft.stantwtpride.prideutils.commands

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.HidePlayersStorageUtil
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.MessageUtil
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ShowCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as Player
        if (args[0] == "*" || args[0] == "all") {
            sender.server.onlinePlayers.forEach {
                player.showPlayer(Init.plugin, it)
            }
            try {
                val store = HidePlayersStorageUtil.getPlayer(player.uniqueId)
                store.storage.hideAll = false
                HidePlayersStorageUtil.playerStorage.players[store.index] = store.storage
            } catch (_: Exception) {}
            sender.sendMessage("${MessageUtil.PrideStart} Successfully shown all players to you! (${ChatColor.RED}${ChatColor.BOLD}WARNING: THIS MAY CAUSE MAJOR LAG FOR YOU AND OTHER PLAYERS!${ChatColor.RESET})")
        } else {
            val showPlayers = args[0].split(",")
            val store = HidePlayersStorageUtil.getPlayer(player.uniqueId)
            showPlayers.forEach {
                val ht = sender.server.onlinePlayers.find { pl -> pl.name == it }
                if (ht != null) {
                    if (store.storage.hiddenPlayers.contains(ht.uniqueId.toString())) {
                        store.storage.hiddenPlayers.remove(ht.uniqueId.toString())
                    }
                    store.storage.shownPlayers.add(ht.uniqueId.toString())

                    player.showPlayer(Init.plugin, ht)
                } else {
                    sender.sendMessage("${MessageUtil.PrideStart} Player $it doesn't exist!")
                }
            }
            sender.sendMessage("${MessageUtil.PrideStart} Successfully shown players ${showPlayers.joinToString(", ")} to you again!")
        }

        HidePlayersStorageUtil.writeToFile()

        return true
    }
}
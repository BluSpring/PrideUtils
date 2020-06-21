package app.hypurrr.minecraft.stantwtpride.prideutils.commands

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.HidePlayersStorageUtil
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.MessageUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object HideCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as Player
        if (args[0] == "*" || args[0] == "all") {
            sender.server.onlinePlayers.forEach {
                player.hidePlayer(Init.plugin, it)
            }
            try {
                val store = HidePlayersStorageUtil.getPlayer(player.uniqueId)
                store.storage.hideAll = true
                HidePlayersStorageUtil.playerStorage.players[store.index] = store.storage
            } catch (_: Exception) {}
            sender.sendMessage("${MessageUtil.PrideStart} Successfully hid all players from you!")
        } else {
            val hidePlayers = args[0].split(",")
            val store = HidePlayersStorageUtil.getPlayer(player.uniqueId)
            hidePlayers.forEach {
                val ht = sender.server.onlinePlayers.find { pl -> pl.name == it }

                if (ht != null) {
                    if (store.storage.shownPlayers.contains(ht.uniqueId.toString())) {
                        store.storage.shownPlayers.remove(ht.uniqueId.toString())
                    }
                    store.storage.hiddenPlayers.add(ht.uniqueId.toString())

                    player.hidePlayer(Init.plugin, ht)
                } else {
                    sender.sendMessage("${MessageUtil.PrideStart} Player $it doesn't exist!")
                }
            }
            sender.sendMessage("${MessageUtil.PrideStart} Successfully hid players ${hidePlayers.joinToString(", ")} from you!")
        }

        HidePlayersStorageUtil.writeToFile()

        return true
    }
}
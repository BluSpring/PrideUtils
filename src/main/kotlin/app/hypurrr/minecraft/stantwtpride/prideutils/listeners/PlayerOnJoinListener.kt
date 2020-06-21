package app.hypurrr.minecraft.stantwtpride.prideutils.listeners

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.HidePlayersStorageUtil
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.MessageUtil
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.types.IPlayerStorage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerOnJoinListener : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player
        val storage = HidePlayersStorageUtil.playerStorage
        val thisStorage = storage.players.find { pl ->
            pl.uuid == player.uniqueId.toString()
        }

        if (thisStorage == null) {
            storage.players.add(IPlayerStorage(
                uuid = player.uniqueId.toString()
            ))
            Init.plugin.server.onlinePlayers.forEach {
                player.hidePlayer(Init.plugin, it)
                val plt = storage.players.find { pl ->
                    pl.uuid == it.player!!.uniqueId.toString()
                }
                    ?: return@forEach

                if (plt.shownPlayers.find { pl -> pl == player.uniqueId.toString() } == null) {
                    it.player!!.hidePlayer(Init.plugin, player)
                }
            }
        }

        if (thisStorage != null && thisStorage.hideAll) {
            Init.plugin.server.onlinePlayers.forEach {
                player.hidePlayer(Init.plugin, it)
                val plt = storage.players.find { pl ->
                    pl.uuid == it.player!!.uniqueId.toString()
                }
                    ?: return@forEach

                if (plt.shownPlayers.find { pl -> pl == player.uniqueId.toString() } == null && plt.hideAll) {
                    it.player!!.hidePlayer(Init.plugin, player)
                }
            }
            player.sendMessage("${MessageUtil.PrideStart} Players were automatically hidden for you in order to decrease lag for you and the server. If you want to turn players back on, please type \"/show *\", or if you want to show certain players, type \"/show <playername>[,<playername>,...]\".")
        } else if (thisStorage != null && !thisStorage.hideAll && thisStorage.hiddenPlayers.isEmpty()) {
            player.sendMessage("${MessageUtil.PrideStart} ${ChatColor.RED}${ChatColor.BOLD}WARNING! ${ChatColor.RESET}All players for you are currently on! This could leave major lag for your game and network if you continue! If you wish to turn off players, please type \"/hide *\"! If you still wish to continue, but only hide certain players, type \"/show <playername>[,<playername>,...]\"")
        }

        if (thisStorage != null && !thisStorage.hideAll && thisStorage.hiddenPlayers.isNotEmpty()) {
            thisStorage.hiddenPlayers.forEach {
                player.hidePlayer(Init.plugin, Init.plugin.server.onlinePlayers.find { pl ->
                    pl.uniqueId.toString() == it
                }!!)
            }
        }

        HidePlayersStorageUtil.writeToFile()
    }
}
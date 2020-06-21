package app.hypurrr.minecraft.stantwtpride.prideutils.commands

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import app.hypurrr.minecraft.stantwtpride.prideutils.player.MusicPlayer
import app.hypurrr.minecraft.stantwtpride.prideutils.storage.MusicMetadatas
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.MessageUtil
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.streams.toList

object PlayCommand : CommandExecutor {
    var player: Player? = null
    var gui: Inventory? = null
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return if (sender.isOp) {
            if (sender is Player) {
                sender.sendMessage("${MessageUtil.PrideStart} Opening menu!")
                player = sender.server.getPlayer(sender.name)
                gui = Bukkit.createInventory(null, 27, "${ChatColor.GREEN}Pick a song!")
                val list: List<MutableMap.MutableEntry<String, JsonElement>> = MusicMetadatas.getMetadatas().asJsonObject.entrySet().stream().toList()
                list.forEachIndexed { i, song ->
                    val songData = song.value.asJsonObject
                    val item: ItemStack?
                    item = if (Init.music.currentlyPlaying != null && !Init.music.currentlyPlaying!!.contains(song.key)) {
                        ItemStack(Material.MUSIC_DISC_BLOCKS).asOne()
                    } else {
                        ItemStack(Material.MUSIC_DISC_CAT).asOne()
                    }
                    val meta = item.itemMeta
                    val minutes = songData.get("duration").asInt / 60
                    val seconds = songData.get("duration").asInt % 60
                    meta.setDisplayName("${ChatColor.AQUA}${songData.get("title")}")
                    meta.lore = mutableListOf("${ChatColor.RED}Uploaded by ${songData.get("uploader").asString}", "${ChatColor.BLUE}$minutes:$seconds", songData.get("short").asString)
                    if (Init.music.currentlyPlaying != null && Init.music.currentlyPlaying!!.contains(song.key)) {
                        (meta.lore as MutableList<String>).add("${ChatColor.GREEN}Currently playing!")
                    }
                    item.itemMeta = meta
                    gui!!.setItem(i, item)
                }
                player!!.openInventory(gui!!)

                true
            } else {
                sender.sendMessage("${MessageUtil.PrideStart} You're not a player!")
                false
            }
        } else {
            sender.sendMessage("${MessageUtil.PrideStart} You don't have permission to use this command!")
            false
        }
    }

    fun onClick(e: InventoryClickEvent) {
        if(gui != null && e.clickedInventory!!.holder == gui!!.holder && e.clickedInventory!!.type == gui!!.type) {
            e.isCancelled = true

            if(e.currentItem != null) {
                val shortened = e.currentItem!!.itemMeta.lore!![2].toString()
                MusicMetadatas.getMetadatas().asJsonObject.entrySet().stream().toList().forEach {
                    if (shortened.contains(it.key)) {
                        player!!.closeInventory()
                        Init.music.playSong(it.key, it.value.asJsonObject.get("duration").asInt, it.value.asJsonObject.get("title").asString, it.value.asJsonObject.get("url").asString)
                        gui = null
                        player = null
                    }
                }

            }
        }
    }
}
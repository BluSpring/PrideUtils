package app.hypurrr.minecraft.stantwtpride.prideutils.player

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import app.hypurrr.minecraft.stantwtpride.prideutils.storage.MusicMetadatas
import org.bukkit.ChatColor
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import java.net.URL
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random
import kotlin.streams.toList

class MusicPlayer {
    private val players: Collection<Player> = Init.plugin.server.onlinePlayers
    var currentlyPlaying: String? = null
    var endTimer: Timer? = Timer()
    fun playSong(song: String, length: Int, title: String, url: String) {
        if(currentlyPlaying != null)
            stopPlayer()
        currentlyPlaying = "pride.music.$song"
        try {
            URL(
                    "https://e79ad983d2fa.ngrok.io/changeSong?password=stantwtpride!&url=$url"
            ).readText()
            // Not gonna do anything with that URL, it's just as a way of saying to the bot to play something.
        } catch (e: Exception) {
            Init.plugin.server.logger.severe("An error has occurred whilst trying to tell the bot to play music!")
            e.printStackTrace()
        }
        players.toList().forEach {
            if (it.player == null)
                return
            val player = it.player!!
            player.sendActionBar("${ChatColor.LIGHT_PURPLE}Now playing - ${ChatColor.RED}$title")
            player.playSound(player.location, "pride.music.$song", SoundCategory.RECORDS, 1000.toFloat(), 1.toFloat())
            Init.plugin.server.logger.info("Playing music!")
        }
        if(endTimer == null)
            endTimer = Timer()
        endTimer!!.schedule((length * 1000).toLong()) {
            val nextToPlay = MusicMetadatas.getMetadatas().asJsonObject.entrySet().stream().toList()
            val rand = Random.nextInt(0, nextToPlay.size)
            val nextSong = nextToPlay[rand].value.asJsonObject
            playSong(nextSong.get("short").asString, nextSong.get("duration").asInt, nextSong.get("title").asString, nextSong.get("url").asString)
        }
    }
    fun stopPlayer() {
        players.toList().forEach {
            val player = it.player!!
            if (it.player == null)
                return
            try {
                if (endTimer != null)
                    endTimer!!.cancel()
                endTimer = null
            } catch (_: Exception) {}
            if (currentlyPlaying != null)
                player.stopSound(currentlyPlaying!!, SoundCategory.RECORDS)
        }

        val i: List<String> = listOf()
        i.find { it == "" }
        for (v in i) {
            continue
        }
    }
}
package app.hypurrr.minecraft.stantwtpride.prideutils.listeners

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import com.destroystokyo.paper.profile.PlayerProfile
import com.destroystokyo.paper.profile.ProfileProperty
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import java.io.File
import java.net.URL
import java.util.*

object PlayerPreLoginListener : Listener {
    @EventHandler
    fun onPreLogin(e: AsyncPlayerPreLoginEvent) {
        Init.plugin.dataFolder.createNewFile()
        val file = File(Init.plugin.dataFolder, "player_cache.json")
        file.createNewFile()
        file.writeText("{}")
        val playerCache = JsonParser().parse(file.reader()).asJsonObject
        val playerStuff: JsonObject?
        playerStuff = try {
            playerCache.get(e.playerProfile.name).asJsonObject
        } catch (_: Exception) {
            null
        }
        if(playerStuff == null) {
            try {
                val actualUsername = URL("https://api.mojang.com/users/profiles/minecraft/${e.name}")
                val conn = actualUsername.openConnection()
                Init.plugin.server.logger.info(conn.content.toString())
            } catch (e: Exception) {
                Init.plugin.logger.severe(e.toString())
            }
        } else {
            e.playerProfile = Init.plugin.server.createProfile(UUID.fromString(playerStuff.get("uuid").asString), playerStuff.get("username").asString)
            e.playerProfile.setProperty(ProfileProperty("textures", playerStuff.get("skin").asString))
        }

        e.allow()
    }
}

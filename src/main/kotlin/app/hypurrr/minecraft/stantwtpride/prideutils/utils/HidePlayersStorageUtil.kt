package app.hypurrr.minecraft.stantwtpride.prideutils.utils

import app.hypurrr.minecraft.stantwtpride.prideutils.Init
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.types.IPlayerInfo
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.types.IPlayerStorage
import app.hypurrr.minecraft.stantwtpride.prideutils.utils.types.IStorage
import com.charleskorn.kaml.Yaml
import java.io.File
import java.util.*

object HidePlayersStorageUtil {
    lateinit var playerStorage: IStorage
    fun loadStorage(): IStorage {
        Init.plugin.logger.info("PrideUtils >> Loading storage...")
        playerStorage = try {
            Yaml.default.parse(IStorage.serializer(), File(Init.plugin.dataFolder, "storage.yml").readText())
        } catch (_: java.lang.Exception) {
            Yaml.default.parse(IStorage.serializer(), this.javaClass.getResource("storage.yml").readText())
        }

        return playerStorage
    }

    fun getPlayer(uuid: UUID): IPlayerInfo {
        return if (playerStorage.players.find { it.uuid == uuid.toString() } == null) {
            val ps = IPlayerStorage(uuid=uuid.toString())
            playerStorage.players.add(ps)

            IPlayerInfo(index=playerStorage.players.size - 1,storage=ps)
        } else {
            val ps = playerStorage.players.find { it.uuid == uuid.toString() }!!
            val index = playerStorage.players.indexOf(ps)

            IPlayerInfo(index=index,storage=ps)
        }
    }

    fun writeToFile() {
        try {
            val file = File(Init.plugin.dataFolder, "storage.yml")

            //file.writeText(Yaml().dump(playerStorage))
            file.writeText(Yaml.default.stringify(IStorage.serializer(), playerStorage))
        } catch (e: Exception) {
            Init.plugin.logger.severe("PrideUtils >> An error has occurred whilst writing storage to file!")
            e.printStackTrace()
        }
    }
}
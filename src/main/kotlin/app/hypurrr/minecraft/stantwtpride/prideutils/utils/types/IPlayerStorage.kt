package app.hypurrr.minecraft.stantwtpride.prideutils.utils.types

import kotlinx.serialization.Serializable

@Serializable
data class IPlayerStorage(
        val uuid: String = "8667ba71b85a4004af54457a9734eed7",
        val hiddenPlayers: MutableList<String> = mutableListOf(),
        val shownPlayers: MutableList<String> = mutableListOf(),
        var hideAll: Boolean = true
)
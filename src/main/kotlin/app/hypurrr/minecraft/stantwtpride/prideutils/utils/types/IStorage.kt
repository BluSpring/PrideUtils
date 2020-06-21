package app.hypurrr.minecraft.stantwtpride.prideutils.utils.types

import kotlinx.serialization.Serializable

@Serializable
data class IStorage(
    val players: MutableList<IPlayerStorage> = mutableListOf()
)
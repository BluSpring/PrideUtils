package app.hypurrr.minecraft.stantwtpride.prideutils.utils.types

import kotlinx.serialization.Serializable

@Serializable
data class IPlayerInfo(
    val index: Int,
    val storage: IPlayerStorage
)
package ru.livetyping.zarina.data.geography.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.geography.Building
import ru.livetyping.zarina.domain.geography.KladrId

@Serializable
data class BuildingDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("text")
    val name: String? = null,
) {
    fun toBuilding(): Building {
        checkNotNull(id) { "id is null" }
        checkNotNull(name) { "name is null" }
        return Building(
            id = KladrId(id),
            name = name,
        )
    }
}

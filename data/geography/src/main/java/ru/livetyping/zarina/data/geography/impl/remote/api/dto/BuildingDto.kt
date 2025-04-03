package ru.livetyping.zarina.data.geography.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import timber.log.Timber

@Serializable
internal data class BuildingDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("text")
    val text: String? = null,
) {
    fun toBuilding(): Building? {
        return if (id != null && text != null) {
            Building(
                id = KladrId(id),
                name = text,
            )
        } else {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to Building")
            null
        }
    }

    private companion object {
        private const val TAG = "BuildingDto"
    }
}

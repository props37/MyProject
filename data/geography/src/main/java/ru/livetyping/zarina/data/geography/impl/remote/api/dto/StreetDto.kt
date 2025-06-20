package ru.livetyping.zarina.data.geography.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.geo.Street
import timber.log.Timber

@Serializable
internal data class StreetDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("text")
    val text: String? = null,
) {
    fun toStreet(): Street? {
        return if (id != null && text != null) {
            Street(
                id = FiasId(id),
                name = text,
            )
        } else {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to Street")
            null
        }
    }

    private companion object {
        private const val TAG = "StreetDto"
    }
}

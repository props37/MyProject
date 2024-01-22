package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Media
import ru.zarina.zarina.domain.rework.common.Url

@Serializable
data class MediaDto(
    @SerialName("media_url")
    val url: String? = null,

    @SerialName("type")
    val type: MediaTypeDto? = null,
) {
    fun toMedia(): Media {
        checkNotNull(url) { "url is null" }
        checkNotNull(type) { "type is null" }
        return Media(
            url = Url(url),
            type = type.toMediaType(),
        )
    }
}

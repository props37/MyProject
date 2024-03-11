package ru.zarina.zarina.data.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.common.Media
import ru.zarina.zarina.domain.common.Url
import timber.log.Timber

@Serializable
data class MediaDto(
    @SerialName("media_url")
    val url: String? = null,

    @SerialName("type")
    val type: MediaTypeDto? = null,
) {
    fun toMedia(): Media? {
        val type = type?.toMediaType()
        return if (url != null && type != null) {
            Media(
                url = Url(url),
                type = type,
            )
        } else {
            Timber.e("Drop Media because its url or type is null")
            null
        }
    }
}

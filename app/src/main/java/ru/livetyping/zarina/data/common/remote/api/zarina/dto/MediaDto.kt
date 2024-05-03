package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.Url
import timber.log.Timber

@Serializable
data class MediaDto(
    @SerialName("media_url")
    val thumbnailUrl: String? = null,

    @SerialName("original_url")
    val originalUrl: String? = null,

    @SerialName("type")
    val type: MediaTypeDto? = null,
) {
    fun toMedia(): Media? {
        val type = type?.toMediaType()
        return if ((originalUrl != null || thumbnailUrl != null) && type != null) {
            val originalUrl = originalUrl?.let { Url(it) }
            val thumbnailUrl = thumbnailUrl?.let { Url(it) }
            Media(
                originalUrl = originalUrl ?: thumbnailUrl ?: Url.EMPTY,
                thumbnailUrl = thumbnailUrl ?: originalUrl ?: Url.EMPTY,
                type = type,
            )
        } else {
            Timber.e("Drop Media because its URLs or type is null")
            null
        }
    }
}

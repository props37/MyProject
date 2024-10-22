package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.media.Media
import timber.log.Timber

@Serializable
public data class MediaDto(
    @SerialName("media_url")
    val mediaUrl: String? = null,

    @SerialName("original_url")
    val originalUrl: String? = null,

    @SerialName("type")
    val type: MediaTypeDto? = null,
) {
    public fun toMedia(): Media? {
        val type = type?.toMediaType()
        return if ((originalUrl != null || mediaUrl != null) && type != null) {
            val originalUrl = originalUrl?.let { Url.create(it) }
            val thumbnailUrl = mediaUrl?.let { Url.create(it) }
            Media(
                originalUrl = originalUrl ?: thumbnailUrl ?: Url.EMPTY,
                thumbnailUrl = thumbnailUrl ?: originalUrl ?: Url.EMPTY,
                type = type,
            )
        } else {
            Timber.tag(TAG).e("Drop MediaDto because its urls or type is null")
            null
        }
    }
}

private const val TAG = "MediaDto"

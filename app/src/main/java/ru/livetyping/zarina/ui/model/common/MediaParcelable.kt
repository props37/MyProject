package ru.livetyping.zarina.ui.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.Url

@Serializable
@Parcelize
data class MediaParcelable(
    val originalUrl: String,
    val thumbnailUrl: String,
    val type: MediaTypeParcelable,
) : Parcelable {
    fun toMedia(): Media = Media(
        originalUrl = Url(originalUrl),
        thumbnailUrl = Url(thumbnailUrl),
        type = type.toMediaType(),
    )

    companion object {
        fun from(media: Media): MediaParcelable = MediaParcelable(
            originalUrl = media.originalUrl.value,
            thumbnailUrl = media.thumbnailUrl.value,
            type = MediaTypeParcelable.from(media.type),
        )
    }
}

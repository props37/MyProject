package ru.livetyping.zarina.core.uimodel.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.media.Media

@Serializable
@Parcelize
public data class MediaParcelable(
    val originalUrl: String,
    val thumbnailUrl: String,
    val type: MediaTypeParcelable,
) : Parcelable {
    public fun toMedia(): Media = Media(
        originalUrl = Url.create(originalUrl),
        thumbnailUrl = Url.create(thumbnailUrl),
        type = type.toMediaType(),
    )

    public companion object {
        public fun from(media: Media): MediaParcelable = MediaParcelable(
            originalUrl = media.originalUrl.value,
            thumbnailUrl = media.thumbnailUrl.value,
            type = MediaTypeParcelable.from(media.type),
        )
    }
}

package ru.livetyping.zarina.ui.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.Url

@Serializable
@Parcelize
data class MediaParcelable(
    val url: String,
    val type: MediaTypeParcelable,
) : Parcelable {
    fun toMedia(): Media = Media(
        url = Url(url),
        type = type.toMediaType(),
    )

    companion object {
        fun from(media: Media): MediaParcelable = MediaParcelable(
            url = media.url.value,
            type = MediaTypeParcelable.from(media.type),
        )
    }
}

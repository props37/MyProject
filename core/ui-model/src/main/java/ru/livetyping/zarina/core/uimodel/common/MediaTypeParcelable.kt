package ru.livetyping.zarina.core.uimodel.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.media.MediaType

@Serializable
@Parcelize
public enum class MediaTypeParcelable : Parcelable {
    IMAGE,
    VIDEO;

    public fun toMediaType(): MediaType = when (this) {
        IMAGE -> MediaType.IMAGE
        VIDEO -> MediaType.VIDEO
    }

    public companion object {
        public fun from(type: MediaType): MediaTypeParcelable = when (type) {
            MediaType.IMAGE -> IMAGE
            MediaType.VIDEO -> VIDEO
        }
    }
}

package ru.livetyping.zarina.presentation.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.MediaType

@Serializable
@Parcelize
enum class MediaTypeParcelable : Parcelable {
    IMAGE,
    VIDEO;

    fun toMediaType(): MediaType = when (this) {
        IMAGE -> MediaType.IMAGE
        VIDEO -> MediaType.VIDEO
    }

    companion object {
        fun from(type: MediaType): MediaTypeParcelable = when (type) {
            MediaType.IMAGE -> IMAGE
            MediaType.VIDEO -> VIDEO
        }
    }
}

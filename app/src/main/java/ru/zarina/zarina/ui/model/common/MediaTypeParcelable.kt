package ru.zarina.zarina.ui.model.common

import ru.zarina.zarina.domain.common.MediaType

enum class MediaTypeParcelable {
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

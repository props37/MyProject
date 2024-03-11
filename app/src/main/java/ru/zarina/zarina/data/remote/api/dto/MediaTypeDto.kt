package ru.zarina.zarina.data.remote.api.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.MediaType
import timber.log.Timber

@Serializable
@JvmInline
value class MediaTypeDto(val value: String) {
    fun toMediaType(): MediaType? = when (value) {
        "image" -> MediaType.IMAGE
        "video" -> MediaType.VIDEO
        else -> {
            Timber.e("Unknown media type $value")
            null
        }
    }
}

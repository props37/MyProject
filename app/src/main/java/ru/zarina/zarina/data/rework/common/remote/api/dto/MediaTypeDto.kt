package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.MediaType

@Serializable
@JvmInline
value class MediaTypeDto(val value: String) {
    fun toMediaType(): MediaType = when (value) {
        "image" -> MediaType.IMAGE
        "video" -> MediaType.VIDEO
        else -> error("Unknown media type $value")
    }
}

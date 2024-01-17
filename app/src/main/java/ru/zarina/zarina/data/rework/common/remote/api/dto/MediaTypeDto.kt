package ru.zarina.zarina.data.rework.common.remote.api.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Media

@Serializable
@JvmInline
value class MediaTypeDto(val value: String) {
    fun toMediaType(): Media.Type = when (value) {
        "image" -> Media.Type.IMAGE
        "video" -> Media.Type.VIDEO
        else -> error("Unknown media type $value")
    }
}

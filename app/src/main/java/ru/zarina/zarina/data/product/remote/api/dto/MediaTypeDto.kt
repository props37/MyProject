package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Media

@JvmInline
@Serializable
value class MediaTypeDto(val value: String) {
    fun toDomain() = when (value) {
        "image" -> Media.Type.IMAGE
        "video" -> Media.Type.VIDEO
        else -> null
    }
}

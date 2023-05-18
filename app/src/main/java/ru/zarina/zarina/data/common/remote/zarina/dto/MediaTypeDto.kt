package ru.zarina.zarina.data.common.remote.zarina.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Media
import timber.log.Timber

@JvmInline
@Serializable
value class MediaTypeDto(val value: String) {
    fun toDomain() = when (value) {
        "image" -> Media.Type.IMAGE
        "video" -> Media.Type.VIDEO
        else -> {
            Timber.w("Unknown media type: $value")
            null
        }
    }
}

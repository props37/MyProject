package ru.livetyping.zarina.data.common.remote.api.zarina.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.MediaType
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

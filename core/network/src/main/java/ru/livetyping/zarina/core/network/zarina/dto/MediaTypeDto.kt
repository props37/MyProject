package ru.livetyping.zarina.core.network.zarina.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.media.MediaType
import timber.log.Timber

@Serializable
@JvmInline
public value class MediaTypeDto(private val value: String) {
    public fun toMediaType(): MediaType? = when (value) {
        "image" -> MediaType.IMAGE
        "video" -> MediaType.VIDEO
        else -> {
            Timber.e("Unknown media type $value")
            null
        }
    }
}

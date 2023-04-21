package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Media

@Serializable
data class MediaDto(
    @SerialName("type")
    val type: MediaTypeDto?,
    @SerialName("media_url")
    val url: String?,
) {
    fun toDomain(): Media? {
        val mediaType = type?.toDomain()
        return if (url == null || mediaType == null)
            null
        else
            Media(url = url, type = mediaType)
    }
}

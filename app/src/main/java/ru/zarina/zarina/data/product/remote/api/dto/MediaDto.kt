package ru.zarina.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Url
import ru.zarina.zarina.utils.kotlin.isNotNull

@Serializable
data class MediaDto(
    @SerialName("type")
    val type: MediaTypeDto?,
    @SerialName("media_url")
    val url: String?,
) {
    fun toDomain(): Media? {
        val mediaType = type?.toDomain()
        val url = url?.let { Url(it) }
        if (
            isNotNull(mediaType, "type")
            && isNotNull(url, "url")
        ) return Media(url = url, type = mediaType)
        return null
    }
}

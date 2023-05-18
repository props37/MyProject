package ru.zarina.zarina.data.common.remote.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Url

@Serializable
data class MediaDto(
    @SerialName("type")
    val type: MediaTypeDto? = null,
    @SerialName("media_url")
    val url: String? = null,
) {
    fun toDomain(): Media? {
        val mediaType = type?.toDomain()
        val url = url?.let { Url(it) }
        if (
            ApiContract.isNotNull(mediaType, "type")
            && ApiContract.isNotNull(url, "url")
        ) return Media(url = url, type = mediaType)
        return null
    }
}

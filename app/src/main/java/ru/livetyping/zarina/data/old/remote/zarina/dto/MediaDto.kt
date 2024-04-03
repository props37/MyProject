package ru.livetyping.zarina.data.old.remote.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.domain.old.Media
import ru.livetyping.zarina.domain.old.Url

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

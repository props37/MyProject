package ru.zarina.zarina.data.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.ApiContract
import ru.zarina.zarina.data.common.remote.zarina.dto.MediaTypeDto
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Media
import ru.zarina.zarina.domain.Url

@Serializable
data class BannerDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("type")
    val type: MediaTypeDto? = null,
    @SerialName("media_url")
    val url: String? = null,
    // TODO click
) {
    fun toDomain(): Banner? {
        val type = type?.toDomain()
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(type, "type")
            && ApiContract.isNotNull(url, "url")
        ) {
            val media = Media(
                type = type,
                url = Url(url),
            )
            return Banner(
                id = id.toString(),
                media = media,
            )
        } else {
            null
        }
    }
}

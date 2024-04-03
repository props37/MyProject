package ru.livetyping.zarina.data.old.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.old.ApiContract
import ru.livetyping.zarina.data.old.remote.zarina.dto.MediaTypeDto
import ru.livetyping.zarina.domain.old.Banner
import ru.livetyping.zarina.domain.old.Media
import ru.livetyping.zarina.domain.old.Url

@Serializable
data class BannerDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("media_type")
    val type: MediaTypeDto? = null,
    @SerialName("media_url")
    val url: String? = null,
    @SerialName("click")
    val click: ActionDto? = null
) {
    fun toDomain(): Banner? {
        val type = type?.toDomain()
        return if (
            ApiContract.isNotNull(id, "id")
            && ApiContract.isNotNull(type, "media_type")
            && ApiContract.isNotNull(url, "media_url")
        ) {
            val media = Media(
                type = type,
                url = Url(url),
            )
            return Banner(
                id = id.toString(),
                media = media,
                action = click?.toDomain()
            )
        } else {
            null
        }
    }
}

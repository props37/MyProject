package ru.livetyping.zarina.feature.home.data.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.media.Media
import ru.livetyping.zarina.core.network.zarina.dto.MediaTypeDto
import ru.livetyping.zarina.feature.home.domain.model.Banner
import timber.log.Timber

@Serializable
internal data class BannerDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("media_type")
    val mediaType: MediaTypeDto? = null,

    @SerialName("media_url")
    val mediaUrl: String? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("click")
    val click: ClickActionDto? = null,
) {
    fun toBanner(): Banner? {
        val mediaType = mediaType?.toMediaType()
        return if (id != null && mediaType != null && mediaUrl != null) {
            val media = Media(url = Url.create(mediaUrl), type = mediaType)
            return Banner(
                id = Banner.Id(id.toString()),
                media = media,
                title = title,
                clickAction = click?.toClickAction(),
            )
        } else {
            Timber.tag(TAG).e("Drop BannerDto because its id, mediaType or mediaUrl is null")
            null
        }
    }
}

private const val TAG = "BannerDto"

package ru.zarina.zarina.data.rework.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.content.HomeBanners

@Serializable
data class HomeBannersDto(
    @SerialName("woman")
    val woman: List<Banner>? = null,
    
    @SerialName("man")
    val man: List<Banner>? = null,
) {
    fun toHomeBanners(): HomeBanners = HomeBanners(
        woman = woman?.map { it.toHomeBanner() } ?: emptyList(),
        man = man?.map { it.toHomeBanner() } ?: emptyList(),
    )

    @Serializable
    data class Banner(
        @SerialName("id")
        val id: Long? = null,

        @SerialName("media_type")
        val mediaType: MediaType? = null,

        @SerialName("media_url")
        val mediaUrl: String? = null,

        @SerialName("view")
        val view: View? = null,
    ) {
        fun toHomeBanner(): HomeBanners.Banner {
            val id = checkNotNull(id) { "id is null" }
            val mediaType = checkNotNull(mediaType) { "mediaType is null" }
            val mediaUrl = checkNotNull(mediaUrl) { "mediaUrl is null" }
            val view = checkNotNull(view) { "view is null" }
            return HomeBanners.Banner(
                id = HomeBanners.Banner.Id(id),
                mediaType = mediaType.toHomeBannerMediaType(),
                mediaUrl = Url(mediaUrl),
                view = view.toHomeBannerView(),
            )
        }

        // TODO: [High] Extract?
        @Serializable
        @JvmInline
        value class MediaType(val value: String) {
            fun toHomeBannerMediaType(): HomeBanners.Banner.MediaType = when (value) {
                "image" -> HomeBanners.Banner.MediaType.IMAGE
                "video" -> HomeBanners.Banner.MediaType.VIDEO
                else -> error("Unknown media type $value")
            }
        }

        // TODO: [High] Extract?
        @Serializable
        @JvmInline
        value class View(val value: String) {
            fun toHomeBannerView(): HomeBanners.Banner.View = when (value) {
                "fullscreen" -> HomeBanners.Banner.View.FULLSCREEN
                "grid" -> HomeBanners.Banner.View.GRID
                else -> error("Unknown view $value")
            }
        }
    }
}

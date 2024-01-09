package ru.zarina.zarina.data.rework.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.content.HomeContent
import ru.zarina.zarina.domain.rework.common.MediaType as DomainMediaType

@Serializable
data class HomeBannersDto(
    @SerialName("woman")
    val woman: List<Banner>? = null,
    
    @SerialName("man")
    val man: List<Banner>? = null,
) {
    fun toHomeContent(): HomeContent = HomeContent(
        womenBanners = woman?.map { it.toHomeContentBanner() } ?: emptyList(),
        menBanners = man?.map { it.toHomeContentBanner() } ?: emptyList(),
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
        fun toHomeContentBanner(): HomeContent.Banner {
            val id = checkNotNull(id) { "id is null" }
            val mediaType = checkNotNull(mediaType) { "mediaType is null" }
            val mediaUrl = checkNotNull(mediaUrl) { "mediaUrl is null" }
            val view = checkNotNull(view) { "view is null" }
            return HomeContent.Banner(
                id = HomeContent.Banner.Id(id),
                mediaType = mediaType.toMediaType(),
                mediaUrl = Url(mediaUrl),
                viewType = view.toHomeBannerView(),
            )
        }

        // TODO: [High] Extract?
        @Serializable
        @JvmInline
        value class MediaType(val value: String) {
            fun toMediaType(): DomainMediaType = when (value) {
                "image" -> DomainMediaType.IMAGE
                "video" -> DomainMediaType.VIDEO
                else -> error("Unknown media type $value")
            }
        }

        // TODO: [High] Extract?
        @Serializable
        @JvmInline
        value class View(val value: String) {
            fun toHomeBannerView(): HomeContent.Banner.ViewType = when (value) {
                "fullscreen" -> HomeContent.Banner.ViewType.FULLSCREEN
                "grid" -> HomeContent.Banner.ViewType.GRID
                else -> error("Unknown view $value")
            }
        }
    }
}

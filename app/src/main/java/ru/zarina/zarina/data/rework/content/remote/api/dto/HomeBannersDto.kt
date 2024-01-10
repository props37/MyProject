package ru.zarina.zarina.data.rework.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.rework.common.remote.api.dto.ClickActionDto
import ru.zarina.zarina.data.rework.common.remote.api.dto.MediaTypeDto
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.content.HomeContent

@Serializable
data class HomeBannersDto(
    @SerialName("woman")
    val womenBanners: List<Banner>? = null,

    @SerialName("man")
    val menBanners: List<Banner>? = null,
) {
    fun toHomeContent(): HomeContent = HomeContent(
        womenBanners = womenBanners?.map { it.toBannerContainer() } ?: emptyList(),
        menBanners = menBanners?.map { it.toBannerContainer() } ?: emptyList(),
    )

    @Serializable
    data class Banner(
        @SerialName("view")
        val viewType: String? = null,

        @SerialName("items")
        val items: List<Item>? = null,
    ) {
        fun toBannerContainer(): HomeContent.BannerContainer {
            checkNotNull(viewType) { "viewType is null" }
            checkNotNull(items) { "items is null" }
            check(items.isNotEmpty()) { "items is empty" }
            val bannerItems = items.map { it.toBanner() }
            return when (viewType) {
                VIEW_TYPE_FULLSCREEN -> {
                    HomeContent.BannerContainer.SingleBanner(bannerItems.first())
                }

                VIEW_TYPE_GRID -> {
                    HomeContent.BannerContainer.MultipleBanners(
                        banners = bannerItems,
                        arrangement = HomeContent.BannerContainer.MultipleBanners.Arrangement.GRID,
                    )
                }

                else -> error("Unknown viewType $viewType")
            }
        }

        @Serializable
        data class Item(
            @SerialName("id")
            val id: Long? = null,

            @SerialName("media_type")
            val mediaType: MediaTypeDto? = null,

            @SerialName("media_url")
            val mediaUrl: String? = null,

            @SerialName("title")
            val title: String? = null,

            @SerialName("click")
            val clickAction: ClickActionDto? = null,
        ) {
            fun toBanner(): HomeContent.Banner {
                checkNotNull(id) { "id is null" }
                checkNotNull(mediaType) { "mediaType is null" }
                checkNotNull(mediaUrl) { "mediaUrl is null" }
                return HomeContent.Banner(
                    id = HomeContent.Banner.Id(id),
                    mediaType = mediaType.toMediaType(),
                    mediaUrl = Url(mediaUrl),
                    title = title,
                    clickAction = clickAction?.toClickAction(),
                )
            }
        }

        companion object {
            private const val VIEW_TYPE_FULLSCREEN = "fullscreen"
            private const val VIEW_TYPE_GRID = "grid"
        }
    }
}

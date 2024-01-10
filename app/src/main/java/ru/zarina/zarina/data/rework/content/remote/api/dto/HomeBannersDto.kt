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
        @SerialName("view")
        val viewType: String? = null,

        @SerialName("items")
        val items: List<Item>? = null,
    ) {
        fun toHomeContentBanner(): HomeContent.Banner {
            checkNotNull(viewType) { "viewType is null" }
            checkNotNull(items) { "items is null" }
            check(items.isNotEmpty()) { "items is empty" }
            val bannerItems = items.map { it.toBannerItem() }
            return when (viewType) {
                VIEW_TYPE_FULLSCREEN -> {
                    HomeContent.Banner.SingleItem(bannerItems.first())
                }

                VIEW_TYPE_GRID -> {
                    HomeContent.Banner.MultipleItems(
                        items = bannerItems,
                        viewType = HomeContent.Banner.MultipleItems.ViewType.GRID,
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
            fun toBannerItem(): HomeContent.Banner.Item {
                checkNotNull(id) { "id is null" }
                checkNotNull(mediaType) { "mediaType is null" }
                checkNotNull(mediaUrl) { "mediaUrl is null" }
                return HomeContent.Banner.Item(
                    id = HomeContent.Banner.Item.Id(id),
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

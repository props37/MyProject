package ru.zarina.zarina.data.rework.content.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.rework.common.remote.api.dto.ClickActionDto
import ru.zarina.zarina.data.rework.common.remote.api.dto.MediaTypeDto
import ru.zarina.zarina.domain.rework.common.Media
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.content.HomeContent
import timber.log.Timber

@Serializable
data class HomeBannersDto(
    @SerialName("woman")
    val womenBanners: List<Banner>? = null,

    @SerialName("man")
    val menBanners: List<Banner>? = null,
) {
    fun toHomeContent(): HomeContent = HomeContent(
        womenBanners = womenBanners?.mapNotNull { it.toBannerContainer() } ?: emptyList(),
        menBanners = menBanners?.mapNotNull { it.toBannerContainer() } ?: emptyList(),
    )

    @Serializable
    data class Banner(
        @SerialName("view")
        val viewType: String? = null,

        @SerialName("items")
        val items: List<Item>? = null,
    ) {
        fun toBannerContainer(): HomeContent.BannerContainer? {
            return if (viewType != null && !items.isNullOrEmpty()) {
                val bannerItems = items.mapNotNull { it.toBanner() }
                return when (viewType) {
                    VIEW_TYPE_FULLSCREEN -> {
                        val first = bannerItems.firstOrNull()
                        if (first != null) {
                            HomeContent.BannerContainer.SingleBanner(first)
                        } else null
                    }

                    VIEW_TYPE_GRID -> {
                        HomeContent.BannerContainer.MultipleBanners(
                            banners = bannerItems,
                            arrangement = HomeContent.BannerContainer.MultipleBanners.Arrangement.GRID,
                        )
                    }

                    else -> {
                        Timber.e("Unknown viewType $viewType")
                        null
                    }
                }
            } else {
                Timber.e("Drop toBannerContainer because its viewType or items is null")
                null
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
            fun toBanner(): HomeContent.Banner? {
                val mediaType = mediaType?.toMediaType()
                return if (id != null && mediaType != null && mediaUrl != null) {
                    val media = Media(url = Url(mediaUrl), type = mediaType)
                    return HomeContent.Banner(
                        id = HomeContent.Banner.Id(id),
                        media = media,
                        title = title,
                        clickAction = clickAction?.toClickAction(),
                    )
                } else {
                    Timber.e("Drop Banner because its ID, mediaType or mediaUrl is null")
                    null
                }
            }
        }

        companion object {
            private const val VIEW_TYPE_FULLSCREEN = "fullscreen"
            private const val VIEW_TYPE_GRID = "grid"
        }
    }
}

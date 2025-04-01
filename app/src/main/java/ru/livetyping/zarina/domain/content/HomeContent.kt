package ru.livetyping.zarina.domain.content

import ru.livetyping.zarina.domain.common.ClickAction
import ru.livetyping.zarina.domain.common.Media
import ru.livetyping.zarina.domain.common.Url

data class HomeContent(
    val womenBanners: List<BannerContainer>,
    val menBanners: List<BannerContainer>,
) {
    sealed class BannerContainer(open val id: Id) {
        data class SingleBanner(val banner: Banner) :
            BannerContainer(id = banner.createBannerContainerId())

        data class MultipleBanners(
            val banners: List<Banner>,
            val arrangement: Arrangement,
        ) : BannerContainer(id = banners.createBannerContainerId()) {
            enum class Arrangement { GRID }
        }

        @JvmInline
        value class Id(val value: String)
    }

    data class Banner(
        val id: Id,
        val title: String?,
        val media: Media,

        /**
         * Image that might be used as a placeholder while the video is loading
         */
        val videoPlaceholder: Url?,
        val clickAction: ClickAction?,
    ) {
        @JvmInline
        value class Id(val value: Long)
    }

    companion object {
        val EMPTY: HomeContent
            get() = HomeContent(womenBanners = emptyList(), menBanners = emptyList())
    }
}

private fun HomeContent.Banner.createBannerContainerId(): HomeContent.BannerContainer.Id {
    return HomeContent.BannerContainer.Id(this.id.value.toString())
}

private fun List<HomeContent.Banner>.createBannerContainerId(): HomeContent.BannerContainer.Id {
    val stringId = this.fold(initial = "") { acc, item ->
        acc + item.id.value.toString()
    }
    return HomeContent.BannerContainer.Id(stringId)
}

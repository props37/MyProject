package ru.zarina.zarina.domain.rework.content

import ru.zarina.zarina.domain.rework.common.Url

data class HomeBanners(
    val womenBanners: List<Banner>,
    val menBanners: List<Banner>,
) {
    data class Banner(
        val id: Id,
        val mediaType: MediaType,
        val mediaUrl: Url,
        val view: View,
    ) {
        @JvmInline
        value class Id(val value: Long)

        // TODO: [Low] Extract?
        enum class MediaType { IMAGE, VIDEO }

        // TODO: [Low] Extract?
        enum class View { FULLSCREEN, GRID }
    }

    companion object {
        val EMPTY: HomeBanners
            get() = HomeBanners(womenBanners = emptyList(), menBanners = emptyList())
    }
}

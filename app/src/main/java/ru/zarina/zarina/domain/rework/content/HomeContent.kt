package ru.zarina.zarina.domain.rework.content

import ru.zarina.zarina.domain.rework.common.MediaType
import ru.zarina.zarina.domain.rework.common.Url

data class HomeContent(
    val womenBanners: List<Banner>,
    val menBanners: List<Banner>,
) {
    data class Banner(
        val id: Id,
        val mediaType: MediaType,
        val mediaUrl: Url,
        val viewType: ViewType,
    ) {
        @JvmInline
        value class Id(val value: Long)

        // TODO: [Low] Extract?
        enum class ViewType { FULLSCREEN, GRID }
    }

    companion object {
        val EMPTY: HomeContent
            get() = HomeContent(womenBanners = emptyList(), menBanners = emptyList())
    }
}

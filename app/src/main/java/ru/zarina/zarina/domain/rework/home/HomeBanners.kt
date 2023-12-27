package ru.zarina.zarina.domain.rework.home

import ru.zarina.zarina.domain.rework.common.Url

data class HomeBanners(
    val woman: List<Banner>,
    val man: List<Banner>,
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
}

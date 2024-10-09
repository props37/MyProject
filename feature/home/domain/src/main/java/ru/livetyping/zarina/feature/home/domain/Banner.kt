package ru.livetyping.zarina.feature.home.domain

import ru.livetyping.zarina.core.domain.media.Media

public data class Banner(
    val id: Id,
    val media: Media,
    val title: String?,
    val clickAction: ClickAction?,
) {
    @JvmInline
    public value class Id(public val value: String)
}

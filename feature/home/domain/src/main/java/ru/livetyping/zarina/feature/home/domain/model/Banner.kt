package ru.livetyping.zarina.feature.home.domain.model

import ru.livetyping.zarina.core.domain.model.media.Media

public data class Banner(
    val id: Id,
    val media: Media,
    val title: String?,
    val clickAction: ClickAction?,
) {
    @JvmInline
    public value class Id(public val value: String)
}

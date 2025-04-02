package ru.livetyping.zarina.feature.home.domain.model

import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.media.Media

public data class Banner(
    val id: Id,
    val title: String?,
    val media: Media,

    /**
     * Image that might be used as a placeholder while the video is loading
     */
    val videoPlaceholderUrl: Url?,
    val clickAction: ClickAction?,
) {
    @JvmInline
    public value class Id(public val value: String)
}

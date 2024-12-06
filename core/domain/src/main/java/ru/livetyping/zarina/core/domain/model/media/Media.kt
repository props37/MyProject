package ru.livetyping.zarina.core.domain.model.media

import ru.livetyping.zarina.core.domain.model.common.Url

// Marked as stable on config/compose/stability_config.txt
public data class Media(
    val originalUrl: Url,
    val thumbnailUrl: Url,
    val type: MediaType,
) {
    public constructor(url: Url, type: MediaType) : this(
        originalUrl = url,
        thumbnailUrl = url,
        type = type,
    )
}

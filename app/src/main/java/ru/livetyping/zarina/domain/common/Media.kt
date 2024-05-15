package ru.livetyping.zarina.domain.common

data class Media(
    val originalUrl: Url,
    val thumbnailUrl: Url,
    val type: MediaType,
) {
    constructor(url: Url, type: MediaType) : this(
        originalUrl = url,
        thumbnailUrl = url,
        type = type,
    )
}

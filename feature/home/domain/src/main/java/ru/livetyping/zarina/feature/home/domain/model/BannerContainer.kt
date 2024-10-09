package ru.livetyping.zarina.feature.home.domain.model

public sealed class BannerContainer(public open val id: Id) {
    @JvmInline
    public value class Id(public val value: String)
}

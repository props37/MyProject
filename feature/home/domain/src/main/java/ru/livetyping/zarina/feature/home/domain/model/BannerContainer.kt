package ru.livetyping.zarina.feature.home.domain.model

public sealed class BannerContainer {
    public abstract val id: Id

    @JvmInline
    public value class Id(public val value: String)
}

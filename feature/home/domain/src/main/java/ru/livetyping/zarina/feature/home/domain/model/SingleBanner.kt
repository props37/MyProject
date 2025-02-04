package ru.livetyping.zarina.feature.home.domain.model

public data class SingleBanner(val banner: Banner) : BannerContainer() {
    override val id: Id = Id(banner.id.value)
}

package ru.livetyping.zarina.feature.home.domain

public data class SingleBanner(val banner: Banner) :
    BannerContainer(id = banner.createBannerContainerId())

private fun Banner.createBannerContainerId(): BannerContainer.Id {
    return BannerContainer.Id(this.id.value)
}

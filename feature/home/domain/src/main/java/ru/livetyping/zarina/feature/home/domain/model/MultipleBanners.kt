package ru.livetyping.zarina.feature.home.domain.model

public data class MultipleBanners(
    val banners: List<Banner>,
    val arrangement: Arrangement,
) : BannerContainer(id = banners.createBannerContainerId()) {
    public enum class Arrangement { GRID }
}

private fun List<Banner>.createBannerContainerId(): BannerContainer.Id {
    val id = this.fold(initial = "") { acc, item -> acc + item.id.value }
    return BannerContainer.Id(id)
}

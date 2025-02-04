package ru.livetyping.zarina.feature.home.domain.model

public data class MultipleBanners(
    val banners: List<Banner>,
    val arrangement: Arrangement,
) : BannerContainer() {
    override val id: Id = banners.createBannerContainerId()

    public enum class Arrangement { GRID }

    private companion object {
        private fun List<Banner>.createBannerContainerId(): Id {
            val id = this.fold(initial = "") { acc, item -> acc + item.id.value }
            return Id(id)
        }
    }
}

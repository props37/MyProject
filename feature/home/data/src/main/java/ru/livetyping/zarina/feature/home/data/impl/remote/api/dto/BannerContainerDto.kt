package ru.livetyping.zarina.feature.home.data.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.feature.home.domain.model.BannerContainer
import ru.livetyping.zarina.feature.home.domain.model.MultipleBanners
import ru.livetyping.zarina.feature.home.domain.model.SingleBanner
import timber.log.Timber

@Serializable
internal data class BannerContainerDto(
    @SerialName("view")
    val viewType: String? = null,

    @SerialName("items")
    val banners: List<BannerDto>? = null,
) {
    fun toBannerContainer(): BannerContainer? {
        return if (viewType != null && !banners.isNullOrEmpty()) {
            val banners = banners.mapNotNull { it.toBanner() }
            return when (viewType) {
                VIEW_TYPE_FULLSCREEN -> {
                    val firstBanner = banners.firstOrNull()
                    firstBanner?.let { SingleBanner(it) }
                }

                VIEW_TYPE_GRID -> {
                    MultipleBanners(
                        banners = banners,
                        arrangement = MultipleBanners.Arrangement.GRID,
                    )
                }

                else -> {
                    Timber.e("Unknown viewType $viewType")
                    null
                }
            }
        } else {
            Timber.e("Drop BannerContainer because its viewType or banners is null")
            null
        }
    }

    private companion object {
        private const val VIEW_TYPE_FULLSCREEN = "fullscreen"
        private const val VIEW_TYPE_GRID = "grid"
    }
}

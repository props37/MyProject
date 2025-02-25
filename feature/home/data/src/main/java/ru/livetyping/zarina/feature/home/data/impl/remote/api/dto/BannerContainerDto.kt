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
    val view: String? = null,

    @SerialName("items")
    val items: List<BannerDto>? = null,
) {
    fun toBannerContainer(): BannerContainer? {
        return if (view != null && !items.isNullOrEmpty()) {
            val banners = items.mapNotNull { it.toBanner() }
            return when (view) {
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
                    Timber.e("Unknown viewType $view")
                    null
                }
            }
        } else {
            Timber.tag(TAG).e("Drop BannerContainerDto $this because its view or items is null")
            null
        }
    }

    private companion object {
        private const val VIEW_TYPE_FULLSCREEN = "fullscreen"
        private const val VIEW_TYPE_GRID = "grid"
    }
}

private const val TAG = "BannerContainerDto"

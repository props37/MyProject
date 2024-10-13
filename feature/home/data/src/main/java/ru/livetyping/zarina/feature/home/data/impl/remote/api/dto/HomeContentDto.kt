package ru.livetyping.zarina.feature.home.data.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

@Serializable
internal data class HomeContentDto(
    @SerialName("woman")
    val womenBanners: List<BannerContainerDto>? = null,

    @SerialName("man")
    val menBanners: List<BannerContainerDto>? = null,
) {
    fun toHomeContent(): HomeContent {
        return HomeContent(
            womenBanners = womenBanners?.mapNotNull { it.toBannerContainer() } ?: emptyList(),
            menBanners = menBanners?.mapNotNull { it.toBannerContainer() } ?: emptyList(),
        )
    }
}

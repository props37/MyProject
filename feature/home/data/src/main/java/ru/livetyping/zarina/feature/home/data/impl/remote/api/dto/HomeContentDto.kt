package ru.livetyping.zarina.feature.home.data.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

@Serializable
internal data class HomeContentDto(
    @SerialName("woman")
    val woman: List<BannerContainerDto>? = null,

    @SerialName("man")
    val man: List<BannerContainerDto>? = null,
) {
    fun toHomeContent(): HomeContent {
        return HomeContent(
            womenBanners = woman?.mapNotNull { it.toBannerContainer() } ?: emptyList(),
            menBanners = man?.mapNotNull { it.toBannerContainer() } ?: emptyList(),
        )
    }
}

package ru.livetyping.zarina.feature.home.data.impl.remote.api

import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.feature.home.data.impl.remote.api.dto.HomeContentDto

internal interface HomeContentApi {
    fun getOnboardingBannerUrl(): Url

    suspend fun getHomeContent(): HomeContentDto
}

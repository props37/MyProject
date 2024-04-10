package ru.livetyping.zarina.data.content.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.content.remote.api.ContentApi
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.content.HomeContent
import javax.inject.Inject

class ContentRemoteDataSource @Inject constructor(
    private val api: ContentApi,
) {
    fun getOnboardingBannerUrl(): Url {
        return api.getOnboardingBannerUrl()
    }

    fun getHomeContentFlow(): Flow<HomeContent> = flow {
        val homeBanners = api.getHomeBanners().toHomeContent()
        emit(homeBanners)
    }
}

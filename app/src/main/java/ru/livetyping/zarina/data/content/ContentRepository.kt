package ru.livetyping.zarina.data.content

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.content.remote.ContentRemoteDataSource
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.content.HomeContent
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val remoteDataSource: ContentRemoteDataSource,
) {
    fun getOnboardingBannerUrl(): Url {
        return remoteDataSource.getOnboardingBannerUrl()
    }

    fun getHomeContentFlow(): Flow<HomeContent> {
        return remoteDataSource.getHomeContentFlow()
    }
}

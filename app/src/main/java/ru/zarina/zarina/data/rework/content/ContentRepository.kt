package ru.zarina.zarina.data.rework.content

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.content.remote.ContentRemoteDataSource
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.content.HomeContent
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

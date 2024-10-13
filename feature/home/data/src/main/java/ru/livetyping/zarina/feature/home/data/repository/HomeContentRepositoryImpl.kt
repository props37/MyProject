package ru.livetyping.zarina.feature.home.data.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.feature.home.data.impl.remote.HomeContentRemoteDataSource
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository
import javax.inject.Inject

internal class HomeContentRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeContentRemoteDataSource,
) : HomeContentRepository {
    override fun getOnboardingBannerUrl(): Url {
        return remoteDataSource.getOnboardingBannerUrl()
    }

    override fun getHomeContentFlow(): Flow<HomeContent> {
        return remoteDataSource.getHomeContentFlow()
    }
}

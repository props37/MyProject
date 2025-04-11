package ru.livetyping.zarina.feature.home.data.impl

import ru.livetyping.zarina.feature.home.data.impl.remote.HomeContentRemoteDataSource
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository
import javax.inject.Inject

internal class HomeContentRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeContentRemoteDataSource,
) : HomeContentRepository {
    override suspend fun getHomeContent(): HomeContent {
        return remoteDataSource.getHomeContent()
    }
}

package ru.livetyping.zarina.feature.home.data.impl.remote

import ru.livetyping.zarina.feature.home.data.impl.remote.api.HomeContentApi
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import javax.inject.Inject

internal class HomeContentRemoteDataSourceImpl @Inject constructor(
    private val api: HomeContentApi,
) : HomeContentRemoteDataSource {
    override suspend fun getHomeContent(): HomeContent {
        return api.getHomeContent().toHomeContent()
    }
}

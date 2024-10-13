package ru.livetyping.zarina.feature.home.data.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.feature.home.data.impl.remote.api.HomeContentApiImpl
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import javax.inject.Inject

internal class HomeContentRemoteDataSourceImpl @Inject constructor(
    private val api: HomeContentApiImpl,
) : HomeContentRemoteDataSource {
    override fun getHomeContentFlow(): Flow<HomeContent> = flow {
        val homeContent = api.getHomeContent().toHomeContent()
        emit(homeContent)
    }
}

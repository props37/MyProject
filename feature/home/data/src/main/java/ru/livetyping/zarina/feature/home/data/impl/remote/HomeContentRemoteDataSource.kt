package ru.livetyping.zarina.feature.home.data.impl.remote

import ru.livetyping.zarina.feature.home.domain.model.HomeContent

internal interface HomeContentRemoteDataSource {
    suspend fun getHomeContent(): HomeContent
}

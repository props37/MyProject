package ru.livetyping.zarina.feature.home.data.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

internal interface HomeContentRemoteDataSource {
    fun getHomeContentFlow(): Flow<HomeContent>
}

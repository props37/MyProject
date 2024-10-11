package ru.livetyping.zarina.feature.home.data.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository
import javax.inject.Inject

internal class HomeContentRepositoryImpl @Inject constructor() : HomeContentRepository {
    override fun getHomeContentFlow(): Flow<HomeContent> {
        TODO("Not yet implemented")
    }
}

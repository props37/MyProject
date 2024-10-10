package ru.livetyping.zarina.feature.home.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

public interface HomeContentRepository {
    public fun getHomeContentFlow(): Flow<HomeContent>
}

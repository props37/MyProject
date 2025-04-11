package ru.livetyping.zarina.feature.home.domain.repository

import ru.livetyping.zarina.feature.home.domain.model.HomeContent

public interface HomeContentRepository {
    public suspend fun getHomeContent(): HomeContent
}

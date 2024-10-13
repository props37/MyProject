package ru.livetyping.zarina.feature.home.data.impl.remote.api

import ru.livetyping.zarina.feature.home.data.impl.remote.api.dto.HomeContentDto

internal interface HomeContentApi {
    suspend fun getHomeContent(): HomeContentDto
}

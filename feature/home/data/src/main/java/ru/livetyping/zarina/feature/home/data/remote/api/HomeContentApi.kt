package ru.livetyping.zarina.feature.home.data.remote.api

import ru.livetyping.zarina.feature.home.data.remote.api.dto.HomeContentDto

internal interface HomeContentApi {
    suspend fun getHomeContent(): HomeContentDto
}

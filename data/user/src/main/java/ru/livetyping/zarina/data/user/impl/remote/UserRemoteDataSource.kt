package ru.livetyping.zarina.data.user.impl.remote

import ru.livetyping.zarina.core.domain.model.geo.City

internal interface UserRemoteDataSource {
    suspend fun setUserCity(city: City)
}

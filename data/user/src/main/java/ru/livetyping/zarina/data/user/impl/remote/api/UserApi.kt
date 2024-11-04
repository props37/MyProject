package ru.livetyping.zarina.data.user.impl.remote.api

import ru.livetyping.zarina.core.domain.model.geo.City

internal interface UserApi {
    suspend fun setUserCity(city: City)
}

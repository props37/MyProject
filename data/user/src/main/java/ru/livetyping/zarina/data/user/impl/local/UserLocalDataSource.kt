package ru.livetyping.zarina.data.user.impl.local

import ru.livetyping.zarina.core.domain.model.geo.City

internal interface UserLocalDataSource {
    suspend fun setUserCity(city: City?)
}

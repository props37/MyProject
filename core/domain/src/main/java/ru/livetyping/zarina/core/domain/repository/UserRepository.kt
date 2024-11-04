package ru.livetyping.zarina.core.domain.repository

import ru.livetyping.zarina.core.domain.model.geo.City

public interface UserRepository {
    public suspend fun setUserCity(city: City)

    public suspend fun setLocalUserCity(city: City)
}

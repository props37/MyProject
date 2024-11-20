package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.User

public interface UserRepository {
    public fun getUserFlow(cachePolicy: CachePolicy): Flow<User?>

    public suspend fun setUserCity(city: City)

    public suspend fun setLocalUserCity(city: City)
}

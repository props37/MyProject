package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.User

public interface UserRepository {
    public fun getUserFlow(cachePolicy: CachePolicy): Flow<User?>

    public suspend fun setUser(user: User)

    public fun getUserCityFlow(cachePolicy: CachePolicy): Flow<City?>

    public suspend fun setUserCity(city: City)

    public suspend fun setLocalUserCity(city: City)

    public fun getLoyaltyCardFlow(cachePolicy: CachePolicy): Flow<LoyaltyCard?>

    // TODO: [Top] Add yandex captcha token
    public suspend fun signIn(
        email: Email,
        password: String,
    ): AuthResult
}

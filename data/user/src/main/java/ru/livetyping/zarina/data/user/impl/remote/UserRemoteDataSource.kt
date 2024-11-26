package ru.livetyping.zarina.data.user.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.AuthResult
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.User

internal interface UserRemoteDataSource {
    fun getUserFlow(): Flow<User>

    fun getUserCityFlow(): Flow<City>

    suspend fun setUserCity(city: City)

    fun getLoyaltyCardFlow(): Flow<LoyaltyCard>

    // TODO: [Top] Add yandex captcha token
    suspend fun signIn(
        email: Email,
        password: String,
    ): AuthResult
}

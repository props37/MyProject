package ru.livetyping.zarina.data.user.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.User

internal interface UserRemoteDataSource {
    fun getUserFlow(): Flow<User>

    suspend fun setUserCity(city: City)

    fun getLoyaltyCardFlow(): Flow<LoyaltyCard>
}

package ru.livetyping.zarina.data.user.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.User

internal interface UserLocalDataSource {
    fun getUserFlow(): Flow<User?>

    suspend fun setUser(user: User)

    fun getUserCityFlow(): Flow<City?>

    suspend fun setUserCity(city: City?)

    fun getLoyaltyCardFlow(): Flow<LoyaltyCard?>

    fun setLoyaltyCard(card: LoyaltyCard?)

    suspend fun clear()
}

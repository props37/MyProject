package ru.livetyping.zarina.data.user.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.User

internal interface UserLocalDataSource {
    fun getUserFlow(): Flow<User?>

    suspend fun setUser(user: User)

    suspend fun setUserCity(city: City?)
}

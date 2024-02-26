package ru.zarina.zarina.data.rework.user.remote

import ru.zarina.zarina.data.rework.user.remote.api.UserApi
import ru.zarina.zarina.domain.rework.geography.City
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val api: UserApi,
) {
    suspend fun updateUserCity(city: City) {
        api.updateUserCity(city)
    }
}

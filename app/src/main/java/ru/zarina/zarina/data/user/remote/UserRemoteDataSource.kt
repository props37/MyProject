package ru.zarina.zarina.data.user.remote

import ru.zarina.zarina.data.user.remote.api.UserApi
import ru.zarina.zarina.domain.geography.City
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val api: UserApi,
) {
    suspend fun setUserCity(city: City) {
        api.setUserCity(city)
    }
}

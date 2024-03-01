package ru.zarina.zarina.data.rework.user

import ru.zarina.zarina.data.rework.user.local.UserLocalDataSource
import ru.zarina.zarina.data.rework.user.remote.UserRemoteDataSource
import ru.zarina.zarina.domain.rework.geography.City
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    suspend fun setUserCity(city: City) {
        remoteDataSource.setUserCity(city)
        localDataSource.setUserCity(city)
    }

    suspend fun clear() {
        localDataSource.clear()
    }
}

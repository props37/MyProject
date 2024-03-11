package ru.zarina.zarina.data.user

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.user.local.UserLocalDataSource
import ru.zarina.zarina.data.user.remote.UserRemoteDataSource
import ru.zarina.zarina.domain.rework.geography.City
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,
) {
    fun getUserCityFlow(): Flow<City?> {
        return localDataSource.getUserCityFlow()
    }

    suspend fun setUserCity(city: City) {
        remoteDataSource.setUserCity(city)
        localDataSource.setUserCity(city)
    }

    suspend fun setLocalUserCity(city: City) {
        localDataSource.setUserCity(city)
    }

    suspend fun clear() {
        localDataSource.clear()
    }
}

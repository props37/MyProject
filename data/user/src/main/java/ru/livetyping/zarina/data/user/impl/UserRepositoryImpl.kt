package ru.livetyping.zarina.data.user.impl

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.data.user.impl.local.UserLocalDataSource
import ru.livetyping.zarina.data.user.impl.remote.UserRemoteDataSource
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
) : UserRepository {
    override suspend fun setUserCity(city: City) {
        remoteDataSource.setUserCity(city)
        localDataSource.setUserCity(city)
    }
}

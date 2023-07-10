package ru.zarina.zarina.data.user

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.user.local.IUserLocalSource
import ru.zarina.zarina.data.user.remote.IUserRemoteSource
import ru.zarina.zarina.domain.City

@Factory
class UserRepository(
    private val remoteSource: IUserRemoteSource,
    private val localSource: IUserLocalSource,
) : IUserRepository {

    override suspend fun setCity(city: City) {
        remoteSource.setCity(city)
        localSource.setCity(city)
    }

    override fun getCity(): Flow<City?> = localSource.getCity()
}

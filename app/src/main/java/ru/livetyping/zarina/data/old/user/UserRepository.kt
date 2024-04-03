package ru.livetyping.zarina.data.old.user

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.user.local.IUserLocalSource
import ru.livetyping.zarina.data.old.user.remote.IUserRemoteSource
import ru.livetyping.zarina.domain.old.City

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

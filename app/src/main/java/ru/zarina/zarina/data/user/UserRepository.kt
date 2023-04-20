package ru.zarina.zarina.data.user

import ru.zarina.zarina.data.user.remote.IUserRemoteSource
import ru.zarina.zarina.domain.City
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteSource: IUserRemoteSource,
) : IUserRepository {

    override suspend fun setCity(city: City) = remoteSource.setCity(city)

}

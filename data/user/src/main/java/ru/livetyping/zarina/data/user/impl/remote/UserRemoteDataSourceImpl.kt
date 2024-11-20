package ru.livetyping.zarina.data.user.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.data.user.impl.remote.api.UserApi
import javax.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val api: UserApi,
) : UserRemoteDataSource {
    override fun getUserFlow(): Flow<User> = flow {
        val user = api.getUser().toUser()
        emit(user)
    }

    override suspend fun setUserCity(city: City) {
        api.setUserCity(city)
    }
}

package ru.livetyping.zarina.data.user.impl.remote

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.data.user.impl.remote.api.UserApi
import javax.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val api: UserApi,
) : UserRemoteDataSource {
    override suspend fun setUserCity(city: City) {
        api.setUserCity(city)
    }
}

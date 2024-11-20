package ru.livetyping.zarina.data.user.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.data.user.impl.local.city.UserCityDataHolder
import javax.inject.Inject

internal class UserLocalDataSourceImpl @Inject constructor(
    private val userCityDataHolder: UserCityDataHolder,
) : UserLocalDataSource {
    override fun getUserFlow(): Flow<User?> {
        TODO("Not yet implemented")
        // TODO: [Top] Implement
    }

    override suspend fun setUser(user: User) {
        TODO("Not yet implemented")
        // TODO: [Top] Implement
    }

    override suspend fun setUserCity(city: City?) {
        userCityDataHolder.setUserCity(city)
    }
}

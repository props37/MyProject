package ru.livetyping.zarina.data.user.impl.local

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.data.user.impl.local.city.UserCityDataHolder
import javax.inject.Inject

internal class UserLocalDataSourceImpl @Inject constructor(
    private val userCityDataHolder: UserCityDataHolder,
) : UserLocalDataSource {
    override suspend fun setUserCity(city: City?) {
        userCityDataHolder.setUserCity(city)
    }
}

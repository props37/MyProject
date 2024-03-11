package ru.zarina.zarina.data.user.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.geography.City
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userCityDataHolder: UserCityDataHolder,
) {
    fun getUserCityFlow(): Flow<City?> {
        return userCityDataHolder.getUserCityFlow()
    }

    suspend fun setUserCity(city: City?) {
        userCityDataHolder.setUserCity(city)
    }

    suspend fun clear() {
        userCityDataHolder.clear()
    }
}

package ru.zarina.zarina.data.user.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.common.Gender
import ru.zarina.zarina.domain.geography.City
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userCityDataHolder: UserCityDataHolder,
    private val userContentGenderDataHolder: UserContentGenderDataHolder,
) {
    fun getUserCityFlow(): Flow<City?> {
        return userCityDataHolder.getUserCityFlow()
    }

    suspend fun setUserCity(city: City?) {
        userCityDataHolder.setUserCity(city)
    }

    fun getUserContentGenderFlow(): Flow<Gender?> {
        return userContentGenderDataHolder.getUserContentGenderFlow()
    }

    suspend fun setUserContentGender(gender: Gender) {
        userContentGenderDataHolder.setUserContentGender(gender)
    }

    suspend fun clear() {
        userCityDataHolder.clear()
    }
}

package ru.livetyping.zarina.data.user.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.user.local.database.dao.UserDao
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.geography.City
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao,
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

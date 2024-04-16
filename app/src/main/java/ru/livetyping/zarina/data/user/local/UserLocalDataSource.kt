package ru.livetyping.zarina.data.user.local

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.data.database.ZarinaDatabase
import ru.livetyping.zarina.data.user.local.database.dao.UserDao
import ru.livetyping.zarina.data.user.local.database.entity.UserEntity
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.user.User
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val database: ZarinaDatabase,
    private val userDao: UserDao,
    private val userCityDataHolder: UserCityDataHolder,
    private val userContentGenderDataHolder: UserContentGenderDataHolder,
) {
    fun getUserFlow(): Flow<User?> {
        return userDao.getUserFlow().map { it?.toUser() }
    }

    suspend fun setUser(user: User) {
        database.withTransaction {
            userDao.clear()
            userDao.saveUser(UserEntity.from(user))
        }
    }

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
        userDao.clear()
        userCityDataHolder.clear()
    }
}

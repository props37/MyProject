package ru.livetyping.zarina.data.user.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.database.transaction.ZarinaDatabaseTransactionManager
import ru.livetyping.zarina.core.database.user.UserDao
import ru.livetyping.zarina.core.database.user.UserEntity
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.data.user.impl.local.city.UserCityDataHolder
import ru.livetyping.zarina.data.user.impl.local.loyaltycard.LoyaltyCardDataHolder
import javax.inject.Inject

internal class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    private val userCityDataHolder: UserCityDataHolder,
    private val loyaltyCardDataHolder: LoyaltyCardDataHolder,
    private val databaseTransactionManager: ZarinaDatabaseTransactionManager,
) : UserLocalDataSource {
    override fun getUserFlow(): Flow<User?> {
        return userDao.getUserFlow().map { it?.toUser() }
    }

    override suspend fun setUser(user: User) {
        databaseTransactionManager.withTransaction {
            userDao.clear()
            userDao.saveUser(UserEntity.from(user))
        }
    }

    override fun getUserCityFlow(): Flow<City?> {
        return userCityDataHolder.getUserCityFlow()
    }

    override suspend fun setUserCity(city: City?) {
        userCityDataHolder.setUserCity(city)
    }

    override fun getLoyaltyCardFlow(): Flow<LoyaltyCard?> {
        return loyaltyCardDataHolder.getLoyaltyCardFlow()
    }

    override fun setLoyaltyCard(card: LoyaltyCard?) {
        loyaltyCardDataHolder.setLoyaltyCard(card)
    }

    override suspend fun clear() {
        userDao.clear()
        loyaltyCardDataHolder.clear()
    }
}

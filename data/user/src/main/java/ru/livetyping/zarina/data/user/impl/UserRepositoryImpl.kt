package ru.livetyping.zarina.data.user.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.domain.cache.CacheExpirationPolicy
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.cache.CacheUpdatePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.data.user.impl.local.UserLocalDataSource
import ru.livetyping.zarina.data.user.impl.remote.UserRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
) : UserRepository {
    override fun getUserFlow(cachePolicy: CachePolicy): Flow<User?> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getUserFlow()
            is CachePolicy.LocalFirstThenRemote -> getUserFlowLocalFirstThenRemote(cachePolicy)
            is CachePolicy.Remote -> getUserFlowRemote(cachePolicy)
        }
    }

    override suspend fun setUserCity(city: City) {
        remoteDataSource.setUserCity(city)
        localDataSource.setUserCity(city)
    }

    override suspend fun setLocalUserCity(city: City) {
        localDataSource.setUserCity(city)
    }

    override fun getLoyaltyCardFlow(cachePolicy: CachePolicy): Flow<LoyaltyCard?> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getLoyaltyCardFlow()
            is CachePolicy.LocalFirstThenRemote -> {
                getLoyaltyCardFlowLocalFirstThenRemote(cachePolicy)
            }

            is CachePolicy.Remote -> getLoyaltyCardFlowRemote(cachePolicy)
        }
    }

    private fun getUserFlowLocalFirstThenRemote(
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<User?> {
        // TODO: [Low] Add support for CacheExpirationPolicy
        Timber.tag(TAG).w("User CacheExpirationPolicy is not supported, fallback to ${CacheExpirationPolicy.UNLIMITED}")
        return localDataSource.getUserFlow().map { cached ->
            if (cached != null) {
                cached
            } else {
                val user = remoteDataSource.getUserFlow().firstOrNull()
                checkNotNull(user) { "Failed to fetch user" }
                when (cachePolicy.updatePolicy) {
                    CacheUpdatePolicy.NONE -> Unit
                    CacheUpdatePolicy.CLEAR -> TODO()
                    CacheUpdatePolicy.UPDATE -> localDataSource.setUser(user)
                }
                user
            }
        }
    }

    private fun getUserFlowRemote(cachePolicy: CachePolicy.Remote): Flow<User?> {
        return remoteDataSource.getUserFlow()
            .onEach { user ->
                when (cachePolicy.updatePolicy) {
                    CacheUpdatePolicy.NONE -> Unit
                    CacheUpdatePolicy.CLEAR -> TODO()
                    CacheUpdatePolicy.UPDATE -> localDataSource.setUser(user)
                }
            }
    }

    private fun getLoyaltyCardFlowLocalFirstThenRemote(
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<LoyaltyCard?> {
        // TODO: [Low] Add support for CacheExpirationPolicy
        Timber.tag(TAG).w("LoyaltyCard CacheExpirationPolicy is not supported, fallback to ${CacheExpirationPolicy.UNLIMITED}")
        return localDataSource.getLoyaltyCardFlow().map { cached ->
            if (cached != null) {
                cached
            } else {
                val loyaltyCard = remoteDataSource.getLoyaltyCardFlow().firstOrNull()
                checkNotNull(loyaltyCard) { "Failed to fetch loyalty card" }
                when (cachePolicy.updatePolicy) {
                    CacheUpdatePolicy.NONE -> Unit
                    CacheUpdatePolicy.CLEAR -> localDataSource.setLoyaltyCard(null)
                    CacheUpdatePolicy.UPDATE -> localDataSource.setLoyaltyCard(loyaltyCard)
                }
                loyaltyCard
            }
        }
    }

    private fun getLoyaltyCardFlowRemote(cachePolicy: CachePolicy.Remote): Flow<LoyaltyCard?> {
        return remoteDataSource.getLoyaltyCardFlow()
            .onEach { loyaltyCard ->
                when (cachePolicy.updatePolicy) {
                    CacheUpdatePolicy.NONE -> Unit
                    CacheUpdatePolicy.CLEAR -> localDataSource.setLoyaltyCard(null)
                    CacheUpdatePolicy.UPDATE -> localDataSource.setLoyaltyCard(loyaltyCard)
                }
            }
    }

    private companion object {
        private const val TAG = "UserRepositoryImpl"
    }
}

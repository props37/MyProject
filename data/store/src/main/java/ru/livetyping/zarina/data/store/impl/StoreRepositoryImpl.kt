package ru.livetyping.zarina.data.store.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.domain.cache.CacheExpirationPolicy
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.cache.CacheUpdatePolicy
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.repository.StoreRepository
import ru.livetyping.zarina.data.store.impl.local.StoreLocalDataSource
import ru.livetyping.zarina.data.store.impl.remote.StoreRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

internal class StoreRepositoryImpl @Inject constructor(
    private val localDataSource: StoreLocalDataSource,
    private val remoteDataSource: StoreRemoteDataSource,
) : StoreRepository {
    override fun getStoresFlow(cachePolicy: CachePolicy): Flow<List<Store>> {
        return when (cachePolicy) {
            CachePolicy.LocalOnly -> localDataSource.getStoresFlow().filterNotNull()
            is CachePolicy.LocalFirstThenRemote -> getStoresFlowLocalFirstThenRemote(cachePolicy)
            is CachePolicy.Remote -> getStoresFlowRemote(cachePolicy)
        }
    }

    override fun clear() {
        localDataSource.clear()
    }

    private fun getStoresFlowLocalFirstThenRemote(
        cachePolicy: CachePolicy.LocalFirstThenRemote,
    ): Flow<List<Store>> {
        // TODO: [Low] Add support for CacheExpirationPolicy
        Timber.tag(TAG).w("Categories CacheExpirationPolicy is not supported, fallback to ${CacheExpirationPolicy.UNLIMITED}")
        return localDataSource.getStoresFlow()
            .map { cached ->
                if (cached != null) {
                    cached
                } else {
                    val stores = remoteDataSource.getStoresFlow().firstOrNull()
                    checkNotNull(stores) { "Failed to fetch stores" }
                    storesCacheUpdatePolicyImpl(stores, cachePolicy.updatePolicy)
                    stores
                }
            }
    }

    private fun getStoresFlowRemote(cachePolicy: CachePolicy.Remote): Flow<List<Store>> {
        return remoteDataSource.getStoresFlow()
            .onEach { stores ->
                storesCacheUpdatePolicyImpl(stores, cachePolicy.updatePolicy)
            }
    }

    private fun storesCacheUpdatePolicyImpl(stores: List<Store>, policy: CacheUpdatePolicy) {
        when (policy) {
            CacheUpdatePolicy.NONE -> Unit
            CacheUpdatePolicy.CLEAR -> localDataSource.setStores(null)
            CacheUpdatePolicy.UPDATE -> localDataSource.setStores(stores)
        }
    }

    private companion object {
        private const val TAG = "StoreRepositoryImpl"
    }
}

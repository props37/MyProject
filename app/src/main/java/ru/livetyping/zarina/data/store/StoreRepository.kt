package ru.livetyping.zarina.data.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.store.local.StoreLocalDataSource
import ru.livetyping.zarina.data.store.remote.StoreRemoteDataSource
import ru.livetyping.zarina.domain.store.Store
import timber.log.Timber
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val localDataSource: StoreLocalDataSource,
    private val remoteDataSource: StoreRemoteDataSource,
) {
    fun getStoresFlow(): Flow<List<Store>> = flow {
        val cached = localDataSource.getStoresFlow().firstOrNull()
        if (cached != null) {
            Timber.v("Get cached stores")
            emit(cached)
        } else {
            val stores = remoteDataSource.getStoresFlow().firstOrNull()
            checkNotNull(stores) { "Failed to fetch stores" }
            localDataSource.setStores(stores)
            emit(stores)
        }
    }

    fun clear() {
        localDataSource.clear()
    }
}

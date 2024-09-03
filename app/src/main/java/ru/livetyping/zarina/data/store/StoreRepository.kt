package ru.livetyping.zarina.data.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.data.store.local.StoreLocalDataSource
import ru.livetyping.zarina.data.store.remote.StoreRemoteDataSource
import ru.livetyping.zarina.domain.store.Store
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val localDataSource: StoreLocalDataSource,
    private val remoteDataSource: StoreRemoteDataSource,
) {
    fun getStoresFlow(): Flow<List<Store>> {
        return localDataSource.getStoresFlow()
            .onEach { cached ->
                if (cached == null) {
                    val stores = remoteDataSource.getStoresFlow().firstOrNull()
                    checkNotNull(stores) { "Failed to fetch stores" }
                    localDataSource.setStores(stores)
                }
            }
            .filterNotNull()
    }

    fun clear() {
        localDataSource.clear()
    }
}

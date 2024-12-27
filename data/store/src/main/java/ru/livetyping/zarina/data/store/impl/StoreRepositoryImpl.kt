package ru.livetyping.zarina.data.store.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.repository.StoreRepository
import ru.livetyping.zarina.data.store.impl.local.StoreLocalDataSource
import ru.livetyping.zarina.data.store.impl.remote.StoreRemoteDataSource
import javax.inject.Inject

internal class StoreRepositoryImpl @Inject constructor(
    private val localDataSource: StoreLocalDataSource,
    private val remoteDataSource: StoreRemoteDataSource,
) : StoreRepository {
    override fun getStoresFlow(cachePolicy: CachePolicy): Flow<List<Store>> {
        // TODO: [Top] Implement
        TODO("Not yet implemented")
    }

    override fun clear() {
        localDataSource.clear()
    }
}

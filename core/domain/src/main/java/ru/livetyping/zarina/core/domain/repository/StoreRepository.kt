package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.store.Store

public interface StoreRepository {
    public fun getStoresFlow(cachePolicy: CachePolicy): Flow<List<Store>>

    public fun clear()
}

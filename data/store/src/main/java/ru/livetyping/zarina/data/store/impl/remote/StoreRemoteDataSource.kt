package ru.livetyping.zarina.data.store.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.store.Store

internal interface StoreRemoteDataSource {
    fun getStoresFlow(): Flow<List<Store>>
}

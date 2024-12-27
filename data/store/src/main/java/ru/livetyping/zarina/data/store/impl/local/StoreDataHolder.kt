package ru.livetyping.zarina.data.store.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.store.Store

internal interface StoreDataHolder {
    fun getStoresFlow(): Flow<List<Store>?>

    fun setStores(stores: List<Store>?)

    fun clear()
}

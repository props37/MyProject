package ru.livetyping.zarina.data.store.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.store.Store
import javax.inject.Inject

class StoreLocalDataSource @Inject constructor(
    private val dataHolder: StoreDataHolder,
) {
    fun getStoresFlow(): Flow<List<Store>?> {
        return dataHolder.getStoresFlow()
    }

    fun setStores(stores: List<Store>) {
        dataHolder.setStores(stores)
    }

    fun clear() {
        dataHolder.clear()
    }
}

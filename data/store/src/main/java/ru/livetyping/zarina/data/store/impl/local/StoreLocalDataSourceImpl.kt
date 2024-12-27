package ru.livetyping.zarina.data.store.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.store.Store
import javax.inject.Inject

internal class StoreLocalDataSourceImpl @Inject constructor(
    private val dataHolder: StoreDataHolder,
) : StoreLocalDataSource {
    override fun getStoresFlow(): Flow<List<Store>?> {
        return dataHolder.getStoresFlow()
    }

    override fun setStores(stores: List<Store>?) {
        dataHolder.setStores(stores)
    }

    override fun clear() {
        dataHolder.clear()
    }
}

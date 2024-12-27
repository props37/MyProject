package ru.livetyping.zarina.data.store.impl.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.domain.model.store.Store
import java.lang.ref.SoftReference
import javax.inject.Inject

internal class StoreDataHolderImpl @Inject constructor() : StoreDataHolder {
    private val stores = MutableStateFlow<SoftReference<List<Store>>?>(null)

    override fun getStoresFlow(): Flow<List<Store>?> {
        return stores.map { it?.get() }
    }

    override fun setStores(stores: List<Store>?) {
        this.stores.value = stores?.let { SoftReference(it) }
    }

    override fun clear() {
        stores.value = null
    }
}

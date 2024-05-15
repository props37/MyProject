package ru.livetyping.zarina.data.store.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.domain.store.Store
import java.lang.ref.SoftReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreDataHolder @Inject constructor() {
    private val stores = MutableStateFlow<SoftReference<List<Store>?>>(SoftReference(null))

    fun getStoresFlow(): Flow<List<Store>?> {
        return stores.map { it.get() }
    }

    fun setStores(stores: List<Store>) {
        this.stores.value = SoftReference(stores)
    }
}

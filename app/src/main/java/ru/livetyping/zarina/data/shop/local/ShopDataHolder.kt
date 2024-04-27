package ru.livetyping.zarina.data.shop.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.domain.shop.Shop
import java.lang.ref.SoftReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopDataHolder @Inject constructor() {
    private val shops = MutableStateFlow<SoftReference<List<Shop>?>>(SoftReference(null))

    fun getShopsFlow(): Flow<List<Shop>?> {
        return shops.map { it.get() }
    }

    fun setShops(shops: List<Shop>) {
        this.shops.value = SoftReference(shops)
    }
}

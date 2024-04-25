package ru.livetyping.zarina.data.shop.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.shop.Shop
import javax.inject.Inject

class ShopLocalDataSource @Inject constructor(
    private val dataHolder: ShopDataHolder,
) {
    fun getShopsFlow(): Flow<List<Shop>?> {
        return dataHolder.getShopsFlow()
    }

    fun setShops(shops: List<Shop>) {
        dataHolder.setShops(shops)
    }
}

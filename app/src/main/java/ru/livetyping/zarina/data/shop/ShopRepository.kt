package ru.livetyping.zarina.data.shop

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.shop.remote.ShopRemoteDataSource
import ru.livetyping.zarina.domain.shop.Shop
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val remoteDataSource: ShopRemoteDataSource,
) {
    fun getShopsFlow(): Flow<List<Shop>> {
        return remoteDataSource.getShopsFlow()
    }
}

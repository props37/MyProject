package ru.livetyping.zarina.data.shop

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.shop.local.ShopLocalDataSource
import ru.livetyping.zarina.data.shop.remote.ShopRemoteDataSource
import ru.livetyping.zarina.domain.shop.Shop
import timber.log.Timber
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val localDataSource: ShopLocalDataSource,
    private val remoteDataSource: ShopRemoteDataSource,
) {
    fun getShopsFlow(): Flow<List<Shop>> = flow {
        val cached = localDataSource.getShopsFlow().firstOrNull()
        if (cached != null) {
            Timber.v("Get cached shops")
            emit(cached)
        } else {
            val shops = remoteDataSource.getShopsFlow().firstOrNull()
            checkNotNull(shops) { "Failed to fetch shops" }
            localDataSource.setShops(shops)
            emit(shops)
        }
    }
}

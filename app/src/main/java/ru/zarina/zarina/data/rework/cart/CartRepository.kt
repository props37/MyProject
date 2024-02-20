package ru.zarina.zarina.data.rework.cart

import ru.zarina.zarina.data.rework.cart.local.CartLocalDataSource
import ru.zarina.zarina.data.rework.cart.remote.CartRemoteDataSource
import ru.zarina.zarina.domain.rework.common.Barcode
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val localDataSource: CartLocalDataSource,
    private val remoteDataSource: CartRemoteDataSource,
) {
    suspend fun addProductToCart(barcode: Barcode, count: Int) {
        remoteDataSource.addProductToCart(barcode, count)
    }
}

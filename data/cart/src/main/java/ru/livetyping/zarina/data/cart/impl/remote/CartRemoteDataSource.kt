package ru.livetyping.zarina.data.cart.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.cart.impl.model.CartProductIds

internal interface CartRemoteDataSource {
    fun getCartProductIdsFlow(): Flow<CartProductIds>
}

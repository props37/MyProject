package ru.livetyping.zarina.data.cart.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.cart.impl.model.CartProductIds
import ru.livetyping.zarina.data.cart.impl.remote.api.CartApi
import javax.inject.Inject

internal class CartRemoteDataSourceImpl @Inject constructor(
    private val api: CartApi,
) : CartRemoteDataSource {
    override fun getCartProductIdsFlow(): Flow<CartProductIds> = flow {
        val cartProductIds = api.getCartProductIds().toCartProductIds()
        emit(cartProductIds)
    }
}

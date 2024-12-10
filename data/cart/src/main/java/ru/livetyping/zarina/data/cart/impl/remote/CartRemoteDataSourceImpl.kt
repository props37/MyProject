package ru.livetyping.zarina.data.cart.impl.remote

import ru.livetyping.zarina.data.cart.impl.remote.api.CartApi
import javax.inject.Inject

internal class CartRemoteDataSourceImpl @Inject constructor(
    private val cartApi: CartApi,
) : CartRemoteDataSource {

}

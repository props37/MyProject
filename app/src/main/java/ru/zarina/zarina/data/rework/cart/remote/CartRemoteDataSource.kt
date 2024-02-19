package ru.zarina.zarina.data.rework.cart.remote

import ru.zarina.zarina.data.rework.cart.remote.api.CartApi
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val api: CartApi,
)

package ru.zarina.zarina.data.rework.cart

import ru.zarina.zarina.data.rework.cart.local.CartLocalDataSource
import ru.zarina.zarina.data.rework.cart.remote.CartRemoteDataSource
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val localDataSource: CartLocalDataSource,
    private val remoteDataSource: CartRemoteDataSource,
)

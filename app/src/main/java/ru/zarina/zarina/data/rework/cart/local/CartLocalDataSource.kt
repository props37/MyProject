package ru.zarina.zarina.data.rework.cart.local

import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CartLocalDataSource @Inject constructor(
    private val dataHolder: CartDataHolder,
) {
    val cartProductCount: StateFlow<Int> = dataHolder.cartProductCount

    fun setCartProductCount(count: Int) {
        dataHolder.setCartProductCount(count)
    }
}

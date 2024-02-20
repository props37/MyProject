package ru.zarina.zarina.data.rework.cart.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartDataHolder @Inject constructor() {
    private val _cartProductCount = MutableStateFlow(0)
    val cartProductCount: StateFlow<Int> = _cartProductCount.asStateFlow()

    fun setCartProductCount(count: Int) {
        _cartProductCount.value = count
    }
}

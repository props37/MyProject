package ru.zarina.zarina.domain.cart

data class CartSize(
    val totalProductCount: Int,
    val deliveryProductCount: Int,
    val pickUpFromShopProductCount: Int,
) {
    companion object {
        val EMPTY: CartSize get() = CartSize(0, 0, 0)
    }
}

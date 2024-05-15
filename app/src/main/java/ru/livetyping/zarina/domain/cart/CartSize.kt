package ru.livetyping.zarina.domain.cart

data class CartSize(
    val totalProductCount: Int,
    val deliveryProductCount: Int,
    val pickUpFromStoreProductCount: Int,
) {
    val isEmpty: Boolean get() = totalProductCount <= 0

    companion object {
        val EMPTY: CartSize get() = CartSize(0, 0, 0)
    }
}

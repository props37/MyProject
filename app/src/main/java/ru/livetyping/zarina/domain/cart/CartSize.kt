package ru.livetyping.zarina.domain.cart

data class CartSize(
    val deliveryProductCount: Int,
    val pickUpFromStoreProductCount: Int,
) {
    companion object {
        val EMPTY: CartSize get() = CartSize(0, 0)
    }
}

package ru.livetyping.zarina.core.domain.model.cart

// Marked as stable on config/compose/stability_config.txt
public data class CartSize(
    val deliveryProductCount: Int,
    val pickupProductCount: Int,
) {
    public companion object {
        public fun getEmpty(): CartSize = CartSize(0, 0)
    }
}

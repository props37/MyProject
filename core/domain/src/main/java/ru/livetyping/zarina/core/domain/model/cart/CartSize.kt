package ru.livetyping.zarina.core.domain.model.cart

// TODO: [High] Add to stability config
public data class CartSize(
    val deliveryProductCount: Int,
    val pickupProductCount: Int,
) {
    public companion object {
        public fun getEmpty(): CartSize = CartSize(0, 0)
    }
}

package ru.livetyping.zarina.domain.cart

data class Cart(
    val products: List<CartProduct>,
    val size: CartSize,
    val price: CartPrice,
    val bonuses: Bonuses,
) {
    data class Bonuses(
        val available: Int,
        val accrualForPurchase: Int,
        val writeOff: WriteOff,
    ) {
        data class WriteOff(
            val isApplied: Boolean,
            val value: Int,
            val max: Int,
        )
    }
}

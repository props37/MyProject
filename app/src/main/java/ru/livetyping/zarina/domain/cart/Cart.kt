package ru.livetyping.zarina.domain.cart

import ru.livetyping.zarina.domain.user.MyCard as DomainMyCard

data class Cart(
    val products: List<CartProduct>,
    val size: CartSize,
    val price: CartPrice,
    val bonuses: Bonuses,
    val myCard: MyCard?,
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

    data class MyCard(
        val number: DomainMyCard.Number,
        val info: String?,
        val isApplied: Boolean,
        val productsFirstPriceSum: Int,
    )
}

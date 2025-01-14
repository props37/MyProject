package ru.livetyping.zarina.core.domain.model.cart

import ru.livetyping.zarina.core.domain.model.giftcert.AppliedGiftCertificate
import ru.livetyping.zarina.core.domain.model.user.MyCard as MyCardDomain

// TODO: [High] Add to stability config

public data class Cart(
    val products: List<CartProduct>,
    val size: CartSize,
    val price: CartPrice,
    val bonusAccount: BonusAccount,
    val myCard: MyCard?,
    val giftCertificate: AppliedGiftCertificate?,
    val promoCode: PromoCode?,
    val productLimit: ProductLimit,
) {
    public data class BonusAccount(
        val balance: Int,
        val addForPurchase: Int,
        val redemption: Redemption,
    ) {
        public data class Redemption(
            val isApplied: Boolean,
            val value: Int,
            val max: Int,
        )
    }

    public data class MyCard(
        val number: MyCardDomain.Number,
        val info: String?,
        val isApplied: Boolean,
        val productsFirstPriceSum: Int,
    )

    public data class PromoCode(
        val isApplied: Boolean,
        val value: String,
    )

    public data class ProductLimit(
        val limit: Int,
        val isExceeded: Boolean,
    )
}

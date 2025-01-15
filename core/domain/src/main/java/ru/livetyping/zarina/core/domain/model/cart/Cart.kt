package ru.livetyping.zarina.core.domain.model.cart

import ru.livetyping.zarina.core.domain.model.giftcert.AppliedGiftCertificate
import ru.livetyping.zarina.core.domain.model.user.MyCard as MyCardDomain

// Marked as stable on config/compose/stability_config.txt
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
    // Marked as stable on config/compose/stability_config.txt
    public data class BonusAccount(
        val balance: Int,
        val addForPurchase: Int,
        val redemption: Redemption,
    ) {
        // Marked as stable on config/compose/stability_config.txt
        public data class Redemption(
            val isApplied: Boolean,
            val value: Int,
            val max: Int,
        )
    }

    // Marked as stable on config/compose/stability_config.txt
    public data class MyCard(
        val number: MyCardDomain.Number,
        val info: String?,
        val isApplied: Boolean,
        val productsFirstPriceSum: Int,
    )

    // Marked as stable on config/compose/stability_config.txt
    public data class PromoCode(
        val isApplied: Boolean,
        val value: String,
    )

    // Marked as stable on config/compose/stability_config.txt
    public data class ProductLimit(
        val limit: Int,
        val isExceeded: Boolean,
    )
}

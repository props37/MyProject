package ru.livetyping.zarina.core.domain.model.cart

import java.math.BigDecimal

// Marked as stable on config/compose/stability_config.txt
public data class CartPrice(
    val cartPrice: BigDecimal,
    val discountSize: BigDecimal,
    val finalPrice: BigDecimal,
    val deliveryPrice: BigDecimal?,
    val giftCertificateRedemptionValue: BigDecimal?,
)

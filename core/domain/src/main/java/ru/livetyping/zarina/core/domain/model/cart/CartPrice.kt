package ru.livetyping.zarina.core.domain.model.cart

// Marked as stable on config/compose/stability_config.txt
public data class CartPrice(
    val cartPrice: Int,
    val discountSize: Int,
    val finalPrice: Int,
    val deliveryPrice: Int?,
    val giftCertificateRedemptionValue: Int?,
)

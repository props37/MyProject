package ru.livetyping.zarina.core.domain.model.cart

// TODO: [High] Add to stability config
public data class CartPrice(
    val cartPrice: Int,
    val discountSize: Int,
    val finalPrice: Int,
    val deliveryPrice: Int?,
    val giftCertificateWriteOffSize: Int?,
)

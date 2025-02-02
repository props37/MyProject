package ru.livetyping.zarina.core.domain.model.giftcert

// Marked as stable on config/compose/stability_config.txt
public data class AppliedGiftCertificate(
    val number: GiftCertificate.Number,
    val balance: Int,
    val redemptionValue: Int,
)

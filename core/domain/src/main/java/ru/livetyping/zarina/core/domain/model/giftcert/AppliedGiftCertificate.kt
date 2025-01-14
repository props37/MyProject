package ru.livetyping.zarina.core.domain.model.giftcert

// TODO: [High] Add to stability config
public data class AppliedGiftCertificate(
    val number: GiftCertificate.Number,
    val balance: Int,
    val redemptionValue: Int,
)

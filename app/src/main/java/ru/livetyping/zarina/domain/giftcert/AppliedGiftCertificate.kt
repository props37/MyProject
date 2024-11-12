package ru.livetyping.zarina.domain.giftcert

data class AppliedGiftCertificate(
    val number: GiftCertificate.Number,
    val balance: Int,
    val writeOffSize: Int,
)

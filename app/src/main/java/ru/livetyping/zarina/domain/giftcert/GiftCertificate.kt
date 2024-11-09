package ru.livetyping.zarina.domain.giftcert

data class GiftCertificate(
    val number: Number,
    val verificationCode: String,
) {
    @JvmInline
    value class Number(val value: String)

    companion object {
        const val NUMBER_MAX_LENGTH = 13
        const val VERIFICATION_CODE_MAX_LENGTH = 5
    }
}

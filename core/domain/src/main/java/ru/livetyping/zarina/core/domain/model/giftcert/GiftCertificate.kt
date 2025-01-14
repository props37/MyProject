package ru.livetyping.zarina.core.domain.model.giftcert

// TODO: [High] Add to stability config

public data class GiftCertificate(
    val number: Number,
    val verificationCode: String,
) {
    @JvmInline
    public value class Number(public val value: String)

    public companion object {
        public const val NUMBER_MAX_LENGTH: Int = 13
        public const val VERIFICATION_CODE_MAX_LENGTH: Int = 5
    }
}

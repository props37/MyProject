package ru.livetyping.zarina.core.domain.model.giftcert

// Marked as stable on config/compose/stability_config.txt
public data class GiftCertificate(
    val number: Number,
    val verificationCode: String,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Number(public val value: String)

    public companion object {
        public const val NUMBER_MAX_LENGTH: Int = 13
        public const val VERIFICATION_CODE_MAX_LENGTH: Int = 5
    }
}

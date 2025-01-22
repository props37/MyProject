package ru.livetyping.zarina.core.domain.model.common

// Marked as stable on config/compose/stability_config.txt
@JvmInline
public value class PhoneNumber private constructor(public val value: String) {
    public companion object {
        public val ZARINA_SUPPORT: PhoneNumber
            get() = create("88007070666")

        public const val MAX_LENGTH: Int = 12

        public fun create(phone: String): PhoneNumber = PhoneNumber(phone.trim())
    }
}

package ru.livetyping.zarina.core.domain.model.common

@JvmInline
public value class PhoneNumber private constructor(public val value: String) {
    public companion object {
        public val ZARINA_SUPPORT: PhoneNumber
            get() = create("88007070666")

        public fun create(phone: String): PhoneNumber = PhoneNumber(phone.trim())
    }
}

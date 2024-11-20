package ru.livetyping.zarina.core.domain.model.common

@JvmInline
public value class Email private constructor(public val value: String) {
    public companion object {
        public fun create(email: String): Email = Email(email.trim())
    }
}

package ru.livetyping.zarina.domain.common

@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        fun create(email: String): Email = Email(email.trim())
    }
}

package ru.livetyping.zarina.core.domain.model.common

// Marked as stable on config/compose/stability_config.txt
@JvmInline
public value class Email private constructor(public val value: String) {
    public companion object {
        public val ZARINA_SUPPORT: Email
            get() = create("help@zarina.ru")

        public fun create(email: String): Email = Email(email.trim())
    }
}

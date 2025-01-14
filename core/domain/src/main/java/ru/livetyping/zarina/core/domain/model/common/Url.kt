package ru.livetyping.zarina.core.domain.model.common

// Marked as stable on config/compose/stability_config.txt
@JvmInline
public value class Url private constructor(public val value: String) {
    public companion object {
        public fun getEmpty(): Url = create("")

        public fun create(url: String): Url = Url(url.trim())
    }
}

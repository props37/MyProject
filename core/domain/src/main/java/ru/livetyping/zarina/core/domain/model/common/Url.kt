package ru.livetyping.zarina.core.domain.model.common

@JvmInline
public value class Url private constructor(public val value: String) {
    public companion object {
        public val EMPTY: Url get() = create("")

        public fun create(url: String): Url = Url(url.trim())
    }
}

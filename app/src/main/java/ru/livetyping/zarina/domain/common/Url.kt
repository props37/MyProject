package ru.livetyping.zarina.domain.common

@JvmInline
value class Url(val value: String) {
    companion object {
        val EMPTY: Url get() = Url("")
    }
}

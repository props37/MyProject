package ru.zarina.zarina.domain.rework.common

@JvmInline
value class Url(val value: String) {
    companion object {
        val EMPTY: Url get() = Url("")
    }
}

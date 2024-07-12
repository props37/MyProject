package ru.livetyping.zarina.domain.user

data class MyCard(
    val number: Number,
) {
    @JvmInline
    value class Number(val value: String)
}

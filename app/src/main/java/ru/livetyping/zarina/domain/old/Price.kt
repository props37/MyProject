package ru.livetyping.zarina.domain.old

data class Price(
    val current: Int,
    val original: Int,
) {
    val isDiscounted = current < original
    val discount = (original.toFloat() - current) / original
}

package ru.zarina.zarina.domain

data class Price(
    val current: Int,
    val original: Int,
) {
    val isDiscounted = current < original
    val discount = (original.toFloat() - current) / original
}

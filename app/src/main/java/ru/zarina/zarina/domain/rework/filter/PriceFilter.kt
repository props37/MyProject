package ru.zarina.zarina.domain.rework.filter

data class PriceFilter(
    val min: Long,
    val max: Long,
) : Filter

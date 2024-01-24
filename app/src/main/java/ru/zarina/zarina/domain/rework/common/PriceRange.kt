package ru.zarina.zarina.domain.rework.common

data class PriceRange(
    val min: Long,
    val max: Long,
) {
    init {
        require(max >= min) { "\"max\" $max must be equal to or larger than \"min\" $min" }
    }
}

package ru.zarina.zarina.domain.common

data class PriceRange(
    val min: Long,
    val max: Long,
) {
    init {
        require(max >= min) { "\"max\" $max must be equal to or larger than \"min\" $min" }
    }

    companion object {
        val EMPTY: PriceRange
            get() = PriceRange(min = 0L, max = Long.MAX_VALUE)
    }
}

package ru.livetyping.zarina.domain.common

data class PriceRange(
    val min: Int,
    val max: Int,
) {
    init {
        require(max >= min) { "\"max\" $max must be equal to or larger than \"min\" $min" }
    }

    companion object {
        val EMPTY: PriceRange
            get() = PriceRange(min = 0, max = Int.MAX_VALUE)
    }
}

package ru.livetyping.zarina.core.domain.model.common

public data class PriceRange(
    val min: Int,
    val max: Int,
) {
    init {
        require(max >= min) { "\"max\" $max must be equal to or larger than \"min\" $min" }
    }

    public companion object {
        public val EMPTY: PriceRange
            get() = PriceRange(min = 0, max = Int.MAX_VALUE)
    }
}

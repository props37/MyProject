package ru.zarina.zarina.domain.rework.filter

data class PriceFilter(
    val min: Long?,
    val max: Long?,
) : Filter {
    init {
        if (min != null && max != null) {
            require(max >= min) { "\"max\" $max must be equal to or larger than \"min\" $min" }
        }
    }

    companion object {
        val EMPTY: PriceFilter
            get() = PriceFilter(min = null, max = null)
    }
}

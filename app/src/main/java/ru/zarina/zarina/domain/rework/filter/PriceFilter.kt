package ru.zarina.zarina.domain.rework.filter

data class PriceFilter(
    val min: Long,
    val max: Long,
) : Filter {
    init {
        require(max >= min) { "\"max\" $max must be equal to or larger than \"min\" $min" }
    }
}

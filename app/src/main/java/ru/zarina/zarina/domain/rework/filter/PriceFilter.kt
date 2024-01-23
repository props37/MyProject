package ru.zarina.zarina.domain.rework.filter

data class PriceFilter(
    val from: Long,
    val to: Long,
    val min: Long,
    val max: Long,
) : Filter {
    init {
        require(to >= from) { "\"to\" $to must be equal to or larger than \"from\" $from" }
        require(from >= min) { "\"from\" $from must be equal to or larger than \"min\" $min" }
        require(to <= max) { "\"to\" $to must be smaller than or equal to \"max\" $max" }
    }
}

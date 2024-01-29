package ru.zarina.zarina.domain.rework.filter

import ru.zarina.zarina.domain.rework.common.PriceRange

data class PriceFilter(
    val min: Long?,
    val max: Long?,
    val limits: PriceRange,
) : Filter {
    init {
        if (min != null && max != null) {
            require(max >= min) { "\"max\" $max must be equal to or larger than \"min\" $min" }
            require(min >= limits.min) { "\"min\" $min must be equal to or larger than \"limits.min\" ${limits.min}" }
            require(max <= limits.max) { "\"max\" $max must be equal to or less than \"limits.max\" ${limits.max}" }
        }
    }

    override val type = Filter.Type.PRICE

    override val isEmpty: Boolean
        get() = (min == null || min == limits.min) && (max == null || max == limits.max)

    companion object {
        val EMPTY: PriceFilter
            get() = PriceFilter(min = null, max = null, limits = PriceRange.EMPTY)
    }
}

fun PriceFilter.combineWith(availableFilter: PriceFilter): PriceFilter {
    return if (this.limits != availableFilter.limits) {
        this.copy(
            min = this.min?.coerceIn(availableFilter.limits.min, availableFilter.limits.max),
            max = this.max?.coerceIn(availableFilter.limits.min, availableFilter.limits.max),
            limits = availableFilter.limits,
        )
    } else this
}

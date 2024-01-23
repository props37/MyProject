package ru.zarina.zarina.domain.rework.filter

data class Filters(
    val price: PriceFilter?,
    val composition: ListFilter<CompositionFilter>?,
    val sizes: ListFilter<SizeFilter>?,
    val colors: ListFilter<ColorFilter>?,
)

package ru.zarina.zarina.domain.rework.filter

// TODO: [High] Add sorting?
data class Filtration(
    val price: PriceFilter?,
    val composition: ListFilter<CompositionFilter>?,
    val sizes: ListFilter<SizeFilter>?,
    val colors: ListFilter<ColorFilter>?,
)

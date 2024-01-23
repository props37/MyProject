package ru.zarina.zarina.domain.rework.filter

data class Filtration(
    val sorting: ListFilter<SortFilter>?,
    val price: PriceFilter?,
    val composition: ListFilter<CompositionFilter>?,
    val sizes: ListFilter<SizeFilter>?,
    val colors: ListFilter<ColorFilter>?,
)

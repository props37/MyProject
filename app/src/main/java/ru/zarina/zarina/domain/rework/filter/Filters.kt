package ru.zarina.zarina.domain.rework.filter

data class Filters(
    val sorting: SortFilter?,
    val price: PriceFilter?,
    val materials: ListFilter<MaterialFilter>?,
    val sizes: ListFilter<SizeFilter>?,
    val colors: ListFilter<ColorFilter>?,
)

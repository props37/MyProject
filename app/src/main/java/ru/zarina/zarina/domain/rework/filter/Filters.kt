package ru.zarina.zarina.domain.rework.filter

data class Filters(
    val sorting: SortFilter?,
    val price: PriceFilter?,
    val materials: ListFilter<MaterialFilterItem>?,
    val sizes: ListFilter<SizeFilterItem>?,
    val colors: ListFilter<ColorFilterItem>?,
)

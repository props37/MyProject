package ru.zarina.zarina.domain.rework.filter

import ru.zarina.zarina.domain.rework.common.PriceRange

data class Filters(
    val sorting: ListFilter<SortFilterItem>?,
    val price: PriceFilter?,
    val priceLimits: PriceRange?, // TODO: [High] Move to PriceFilter?
    val materials: ListFilter<MaterialFilterItem>?,
    val sizes: ListFilter<SizeFilterItem>?,
    val colors: ListFilter<ColorFilterItem>?,
)

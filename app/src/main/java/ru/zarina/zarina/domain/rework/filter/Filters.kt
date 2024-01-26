package ru.zarina.zarina.domain.rework.filter

import ru.zarina.zarina.domain.rework.common.Sorting

data class Filters(
    val sorting: ListFilter<SortFilterItem>?,
    val price: PriceFilter?,
    val materials: ListFilter<MaterialFilterItem>?,
    val sizes: ListFilter<SizeFilterItem>?,
    val colors: ListFilter<ColorFilterItem>?,
) : Iterable<Filter> {
    override fun iterator(): Iterator<Filter> = iterator {
        if (sorting != null) yield(sorting)
        if (price != null) yield(price)
        if (materials != null) yield(materials)
        if (sizes != null) yield(sizes)
        if (colors != null) yield(colors)
    }

    val size: Int by lazy {
        var size = 0
        if (sorting != null) size++
        if (price != null) size++
        if (materials != null) size++
        if (sizes != null) size++
        if (colors != null) size++
        size
    }

    companion object {
        val EMPTY: Filters
            get() = Filters(
                sorting = null,
                price = null,
                materials = null,
                sizes = null,
                colors = null,
            )

        fun create(
            sorting: ListFilter<SortFilterItem>? = null,
            price: PriceFilter? = null,
            materials: ListFilter<MaterialFilterItem>? = null,
            sizes: ListFilter<SizeFilterItem>? = null,
            colors: ListFilter<ColorFilterItem>? = null,
        ): Filters = Filters(
            sorting = sorting,
            price = price,
            materials = materials,
            sizes = sizes,
            colors = colors,
        )

        fun getDefaultSorting(selected: Sorting? = null): ListFilter<SortFilterItem> {
            val items = Sorting.entries.map {
                SortFilterItem.from(
                    sorting = it,
                    isSelected = it == selected,
                )
            }
            return ListFilter(items = items, isSingleSelection = true, type = Filter.Type.SORTING)
        }
    }
}

fun Filters.coerceIn(available: Filters): Filters {
    val price = available.price?.let { this.price?.coerceIn(it) ?: it }
    val materials = available.materials?.let { this.materials?.coerceIn(it) ?: it }
    val sizes = available.sizes?.let { this.sizes?.coerceIn(it) ?: it }
    val colors = available.colors?.let { this.colors?.coerceIn(it) ?: it }
    return copy(
        price = price,
        materials = materials,
        sizes = sizes,
        colors = colors,
    )
}

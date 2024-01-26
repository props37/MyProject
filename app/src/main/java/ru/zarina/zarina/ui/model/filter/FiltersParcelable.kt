package ru.zarina.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.filter.Filter
import ru.zarina.zarina.domain.rework.filter.Filters
import ru.zarina.zarina.domain.rework.filter.ListFilter

@Serializable
@Parcelize
data class FiltersParcelable(
    val sorting: ListFilterParcelable?,
    val price: PriceFilterParcelable?,
    val materials: ListFilterParcelable?,
    val sizes: ListFilterParcelable?,
    val colors: ListFilterParcelable?,
) : Parcelable {
    fun toFilters(): Filters {
        return Filters(
            sorting = sorting?.let { sorting ->
                ListFilter(
                    items = sorting.items.map { it.toSortFilterItem() },
                    isSingleSelection = sorting.isSingleSelection,
                    type = Filter.Type.SORTING,
                )
            },
            price = price?.toPriceFilter(),
            materials = materials?.let { materials ->
                ListFilter(
                    items = materials.items.map { it.toMaterialFilterItem() },
                    isSingleSelection = materials.isSingleSelection,
                    type = Filter.Type.MATERIALS,
                )
            },
            sizes = sizes?.let { sizes ->
                ListFilter(
                    items = sizes.items.map { it.toSizeFilterItem() },
                    isSingleSelection = sizes.isSingleSelection,
                    type = Filter.Type.SIZES,
                )
            },
            colors = colors?.let { colors ->
                ListFilter(
                    items = colors.items.map { it.toColorFilterItem() },
                    isSingleSelection = colors.isSingleSelection,
                    type = Filter.Type.COLORS,
                )
            },
        )
    }

    companion object {
        fun from(filters: Filters): FiltersParcelable {
            return FiltersParcelable(
                sorting = filters.sorting?.let { ListFilterParcelable.from(it) },
                price = filters.price?.let { PriceFilterParcelable.from(it) },
                materials = filters.materials?.let { ListFilterParcelable.from(it) },
                sizes = filters.sizes?.let { ListFilterParcelable.from(it) },
                colors = filters.colors?.let { ListFilterParcelable.from(it) },
            )
        }
    }
}

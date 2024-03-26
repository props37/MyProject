package ru.zarina.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.filter.Filter
import ru.zarina.zarina.domain.filter.Filters
import ru.zarina.zarina.domain.filter.ListFilter
import ru.zarina.zarina.domain.filter.ToggleFilter

@Serializable
@Parcelize
data class FiltersParcelable(
    val sorting: ListFilterParcelable?,
    val price: PriceFilterParcelable?,
    val materials: ListFilterParcelable?,
    val sizes: ListFilterParcelable?,
    val colors: ListFilterParcelable?,
    val deliveryAvailability: Boolean?,
    val storePickupAvailability: Boolean?,
) : Parcelable {
    fun toFilters(): Filters {
        val sorting = sorting?.let { sorting ->
            ListFilter(
                items = sorting.items.map { it.toSortFilterItem() },
                isSingleSelection = sorting.isSingleSelection,
                type = sorting.type.toFilterType(),
            )
        }
        val materials = materials?.let { materials ->
            ListFilter(
                items = materials.items.map { it.toMaterialFilterItem() },
                isSingleSelection = materials.isSingleSelection,
                type = materials.type.toFilterType(),
            )
        }
        val sizes = sizes?.let { sizes ->
            ListFilter(
                items = sizes.items.map { it.toSizeFilterItem() },
                isSingleSelection = sizes.isSingleSelection,
                type = sizes.type.toFilterType(),
            )
        }
        val colors = colors?.let { colors ->
            ListFilter(
                items = colors.items.map { it.toColorFilterItem() },
                isSingleSelection = colors.isSingleSelection,
                type = colors.type.toFilterType(),
            )
        }
        val deliveryAvailability = deliveryAvailability?.let {
            ToggleFilter(isEnabled = it, type = Filter.Type.DELIVERY_AVAILABILITY)
        }
        val storePickupAvailability = storePickupAvailability?.let {
            ToggleFilter(isEnabled = it, type = Filter.Type.STORE_PICKUP_AVAILABILITY)
        }
        return Filters(
            sorting = sorting,
            price = price?.toPriceFilter(),
            materials = materials,
            sizes = sizes,
            colors = colors,
            deliveryAvailability = deliveryAvailability,
            storePickupAvailability = storePickupAvailability,
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
                deliveryAvailability = filters.deliveryAvailability?.isEnabled,
                storePickupAvailability = filters.storePickupAvailability?.isEnabled,
            )
        }
    }
}

package ru.zarina.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.common.Color
import ru.zarina.zarina.domain.rework.filter.ColorFilterItem
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.domain.rework.filter.ListFilterItem
import ru.zarina.zarina.domain.rework.filter.MaterialFilterItem
import ru.zarina.zarina.domain.rework.filter.SizeFilterItem
import ru.zarina.zarina.domain.rework.filter.SortFilterItem

@Serializable
@Parcelize
class ListFilterParcelable(
    val items: List<Item>,
    val isSingleSelection: Boolean,
    val type: FilterTypeParcelable,
) : Parcelable {

    fun toListFilter(): ListFilter<ListFilterItem> {
        val items = when (type) {
            FilterTypeParcelable.SORTING -> items.map { it.toSortFilterItem() }
            FilterTypeParcelable.MATERIALS -> items.map { it.toMaterialFilterItem() }
            FilterTypeParcelable.SIZES -> items.map { it.toSizeFilterItem() }
            FilterTypeParcelable.COLORS -> items.map { it.toColorFilterItem() }

            FilterTypeParcelable.PRICE -> {
                error("Could not map ${FilterTypeParcelable.PRICE} filter to ListFilter")
            }

            FilterTypeParcelable.DELIVERY_AVAILABILITY -> {
                error("Could not map ${FilterTypeParcelable.DELIVERY_AVAILABILITY} filter to ListFilter")
            }

            FilterTypeParcelable.STORE_PICKUP_AVAILABILITY -> {
                error("Could not map ${FilterTypeParcelable.STORE_PICKUP_AVAILABILITY} filter to ListFilter")
            }
        }
        return ListFilter(
            items = items,
            isSingleSelection = isSingleSelection,
            type = type.toFilterType(),
        )
    }

    @Serializable
    @Parcelize
    class Item(
        val id: String,
        val name: String,
        val isSelected: Boolean,
        val color: String?,
    ) : Parcelable {
        fun toSortFilterItem(): SortFilterItem = SortFilterItem(
            id = ListFilterItem.Id(id),
            name = name,
            isSelected = isSelected,
        )

        fun toMaterialFilterItem(): MaterialFilterItem = MaterialFilterItem(
            id = ListFilterItem.Id(id),
            name = name,
            isSelected = isSelected,
        )

        fun toSizeFilterItem(): SizeFilterItem = SizeFilterItem(
            id = ListFilterItem.Id(id),
            name = name,
            isSelected = isSelected,
        )

        fun toColorFilterItem(): ColorFilterItem {
            checkNotNull(color) { "color is null" }
            return ColorFilterItem(
                id = ListFilterItem.Id(id),
                name = name,
                isSelected = isSelected,
                color = Color(color),
            )
        }

        companion object {
            fun from(item: ListFilterItem): Item = Item(
                id = item.id.value,
                name = item.name,
                isSelected = item.isSelected,
                color = if (item is ColorFilterItem) item.color.value else null,
            )
        }
    }

    companion object {
        fun from(listFilter: ListFilter<*>): ListFilterParcelable {
            return ListFilterParcelable(
                items = listFilter.items.map { Item.from(it) },
                isSingleSelection = listFilter.isSingleSelection,
                type = FilterTypeParcelable.from(listFilter.type),
            )
        }
    }
}

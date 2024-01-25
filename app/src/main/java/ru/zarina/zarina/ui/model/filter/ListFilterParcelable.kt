package ru.zarina.zarina.ui.model.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.rework.filter.ColorFilterItem
import ru.zarina.zarina.domain.rework.filter.ListFilter
import ru.zarina.zarina.domain.rework.filter.ListFilterItem

@Serializable
@Parcelize
class ListFilterParcelable(
    val items: List<Item>,
    val isSingleSelection: Boolean,
) : Parcelable {

    @Serializable
    @Parcelize
    class Item(
        val id: String,
        val name: String,
        val isSelected: Boolean,
        val color: String?,
    ) : Parcelable {
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
            )
        }
    }
}

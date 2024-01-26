package ru.zarina.zarina.domain.rework.filter

import ru.zarina.zarina.domain.rework.common.Sorting

data class SortFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ListFilterItem(id, name, isSelected) {
    companion object {
        fun from(sorting: Sorting, isSelected: Boolean = false): SortFilterItem = SortFilterItem(
            id = Id(sorting.name),
            name = sorting.name,
            isSelected = isSelected,
        )
    }
}

val SortFilterItem.sorting: Sorting
    get() = Sorting.valueOf(name)

val ListFilter<SortFilterItem>.selected: Sorting?
    get() = this.selectedItems.firstOrNull()?.sorting

package ru.zarina.zarina.domain.rework.filter

import ru.zarina.zarina.domain.rework.common.Sorting

data class SortFilter(val items: List<Sorting>) : Filter {
    data class Item(
        val sorting: Sorting,
        val isSelected: Boolean,
    )
}

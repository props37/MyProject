package ru.zarina.zarina.domain.rework.filter

data class ListFilter<T : ListFilter.Item>(
    val items: List<T>,
    val isSingleSelection: Boolean,
) : Filter {
    sealed class Item(
        open val id: Id,
        open val name: String,
        open val isSelected: Boolean,
    ) {
        @JvmInline
        value class Id(val value: String)
    }
}

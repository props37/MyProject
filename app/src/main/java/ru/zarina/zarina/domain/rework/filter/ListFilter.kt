package ru.zarina.zarina.domain.rework.filter

data class ListFilter<T : ListFilterItem>(
    val items: List<T>,
    val isSingleSelection: Boolean,
    override val type: Filter.Type,
) : Filter {
    val selectedItems by lazy { items.filter { it.isSelected } }
}

sealed class ListFilterItem(
    open val id: Id,
    open val name: String,
    open val isSelected: Boolean,
) {
    @JvmInline
    value class Id(val value: String)
}

fun <T : ListFilterItem> ListFilter<T>.coerceIn(available: ListFilter<T>): ListFilter<T> {
    val items = this.items.filter { item ->
        val isAvailable = available.items.find { it.id == item.id } != null
        isAvailable
    }
    return this.copy(
        items = items,
        isSingleSelection = available.isSingleSelection,
    )
}

package ru.zarina.zarina.domain.rework.filter

data class SortFilter(
    override val id: Id,
    override val name: String,
) : ListFilter.Item(id, name)

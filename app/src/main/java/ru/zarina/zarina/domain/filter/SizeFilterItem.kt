package ru.zarina.zarina.domain.filter

data class SizeFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ListFilterItem(id, name, isSelected)

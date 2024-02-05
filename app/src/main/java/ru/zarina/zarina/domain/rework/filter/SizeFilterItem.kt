package ru.zarina.zarina.domain.rework.filter

data class SizeFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ListFilterItem(id, name, isSelected)

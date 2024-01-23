package ru.zarina.zarina.domain.rework.filter

data class MaterialFilter(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ListFilterItem(id, name, isSelected)

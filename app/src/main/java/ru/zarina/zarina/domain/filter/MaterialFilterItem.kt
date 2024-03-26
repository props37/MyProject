package ru.zarina.zarina.domain.filter

data class MaterialFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ListFilterItem(id, name, isSelected)

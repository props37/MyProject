package ru.zarina.zarina.domain.rework.filter

data class CompositionFilter(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ListFilter.Item(id, name, isSelected)

package ru.zarina.zarina.domain.rework.filter

data class CompositionFilter(
    override val id: Id,
    override val name: String,
) : ListFilter.Item(id, name)

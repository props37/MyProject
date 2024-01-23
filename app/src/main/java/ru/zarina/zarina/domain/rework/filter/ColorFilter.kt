package ru.zarina.zarina.domain.rework.filter

import ru.zarina.zarina.domain.rework.common.Color

data class ColorFilter(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
    val color: Color,
) : ListFilterItem(id, name, isSelected)

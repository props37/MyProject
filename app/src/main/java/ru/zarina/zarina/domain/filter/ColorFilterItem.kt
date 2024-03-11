package ru.zarina.zarina.domain.filter

import ru.zarina.zarina.domain.common.Color

data class ColorFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
    val color: Color,
) : ListFilterItem(id, name, isSelected)

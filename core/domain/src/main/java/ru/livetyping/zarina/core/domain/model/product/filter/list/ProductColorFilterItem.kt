package ru.livetyping.zarina.core.domain.model.product.filter.list

import ru.livetyping.zarina.core.domain.model.common.Color

public data class ProductColorFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
    val color: Color?,
) : ProductListFilterItem(id, name, isSelected)

package ru.livetyping.zarina.core.domain.model.product.filter.list

import ru.livetyping.zarina.core.domain.model.common.Color

// Marked as stable on config/compose/stability_config.txt
public data class ProductColorFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
    val color: Color?,
) : ProductListFilterItem()

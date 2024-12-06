package ru.livetyping.zarina.core.domain.model.product.filter.list

public data class ProductSizeFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ProductListFilterItem(id, name, isSelected)

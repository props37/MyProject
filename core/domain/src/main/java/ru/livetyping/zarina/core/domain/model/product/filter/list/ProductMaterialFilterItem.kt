package ru.livetyping.zarina.core.domain.model.product.filter.list

// Marked as stable on config/compose/stability_config.txt
public data class ProductMaterialFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ProductListFilterItem(id, name, isSelected)

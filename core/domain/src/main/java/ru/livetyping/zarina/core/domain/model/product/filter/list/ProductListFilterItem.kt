package ru.livetyping.zarina.core.domain.model.product.filter.list

// Marked as stable on config/compose/stability_config.txt
public sealed class ProductListFilterItem {
    public abstract val id: Id
    public abstract val name: String
    public abstract val isSelected: Boolean

    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)
}

public fun ProductListFilterItem.copy(isSelected: Boolean): ProductListFilterItem {
    return when (this) {
        is ProductColorFilterItem -> this.copy(isSelected = isSelected)
        is ProductMaterialFilterItem -> this.copy(isSelected = isSelected)
        is ProductPickupStoreFilterItem -> this.copy(isSelected = isSelected)
        is ProductSizeFilterItem -> this.copy(isSelected = isSelected)
        is ProductSortFilterItem -> this.copy(isSelected = isSelected)
    }
}

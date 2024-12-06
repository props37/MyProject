package ru.livetyping.zarina.core.domain.model.product.filter.list

public sealed class ProductListFilterItem(
    public open val id: Id,
    public open val name: String,
    public open val isSelected: Boolean,
) {
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

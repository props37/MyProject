package ru.livetyping.zarina.core.domain.model.product.filter.list

import ru.livetyping.zarina.core.domain.model.product.ProductSorting

public data class ProductSortFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ProductListFilterItem(id, name, isSelected) {
    public companion object {
        public fun from(
            sorting: ProductSorting,
            isSelected: Boolean = false,
        ): ProductSortFilterItem {
            return ProductSortFilterItem(
                id = Id(sorting.name),
                name = sorting.name,
                isSelected = isSelected,
            )
        }
    }
}

public val ProductSortFilterItem.sorting: ProductSorting
    get() = ProductSorting.valueOf(name)

public val ProductListFilter<ProductSortFilterItem>.selected: ProductSorting?
    get() = this.selectedItems.firstOrNull()?.sorting

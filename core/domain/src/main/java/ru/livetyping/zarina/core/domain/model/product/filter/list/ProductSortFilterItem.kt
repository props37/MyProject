package ru.livetyping.zarina.core.domain.model.product.filter.list

import ru.livetyping.zarina.core.domain.model.product.ProductSorting

// Marked as stable on config/compose/stability_config.txt
public data class ProductSortFilterItem(
    override val id: Id,
    override val name: String,
    override val isSelected: Boolean,
) : ProductListFilterItem() {
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

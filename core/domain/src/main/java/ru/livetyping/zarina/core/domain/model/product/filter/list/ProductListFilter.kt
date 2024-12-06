package ru.livetyping.zarina.core.domain.model.product.filter.list

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter

// Marked as stable on config/compose/stability_config.txt
public data class ProductListFilter<T : ProductListFilterItem>(
    val items: List<T>,
    val isSingleSelection: Boolean,
    override val type: ProductFilter.Type,
) : ProductFilter<ProductListFilter<T>> {
    override val isApplied: Boolean by lazy {
        items.any { it.isSelected }
    }

    override val isEmpty: Boolean by lazy { items.isEmpty() }

    val selectedItems: List<T> by lazy {
        items.filter { it.isSelected }
    }

    override fun coerceInAvailable(available: ProductListFilter<T>): ProductListFilter<T> {
        val items = this.items.filter { item ->
            val isAvailable = available.items.find { it.id == item.id } != null
            isAvailable
        }
        return this.copy(
            items = items,
            isSingleSelection = available.isSingleSelection,
        )
    }
}

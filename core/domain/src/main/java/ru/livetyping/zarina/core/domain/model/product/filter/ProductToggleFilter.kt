package ru.livetyping.zarina.core.domain.model.product.filter

// Marked as stable on config/compose/stability_config.txt
public data class ProductToggleFilter(
    val isEnabled: Boolean,
    override val type: ProductFilter.Type,
) : ProductFilter<ProductToggleFilter> {
    override val isApplied: Boolean get() = isEnabled

    override val isEmpty: Boolean get() = false

    override fun coerceInAvailable(available: ProductToggleFilter): ProductToggleFilter = this
}

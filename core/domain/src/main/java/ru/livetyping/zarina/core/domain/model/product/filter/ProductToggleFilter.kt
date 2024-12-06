package ru.livetyping.zarina.core.domain.model.product.filter

public data class ProductToggleFilter(
    val isEnabled: Boolean,
    override val type: ProductFilter.Type,
) : ProductFilter<ProductToggleFilter> {
    override val isApplied: Boolean get() = isEnabled

    override val isEmpty: Boolean get() = false

    override fun coerceInAvailable(available: ProductToggleFilter): ProductToggleFilter = this
}

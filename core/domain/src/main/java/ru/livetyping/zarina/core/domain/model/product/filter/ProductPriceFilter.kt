package ru.livetyping.zarina.core.domain.model.product.filter

import ru.livetyping.zarina.core.domain.model.common.PriceRange

// Marked as stable on config/compose/stability_config.txt
public data class ProductPriceFilter(
    val min: Int?,
    val max: Int?,
    val limits: PriceRange,
) : ProductFilter<ProductPriceFilter> {
    init {
        if (min != null && max != null) {
            require(max >= min) { "\"max\" $max must be equal to or larger than \"min\" $min" }
            require(min >= limits.min) { "\"min\" $min must be equal to or larger than \"limits.min\" ${limits.min}" }
            require(max <= limits.max) { "\"max\" $max must be equal to or less than \"limits.max\" ${limits.max}" }
        }
    }

    override val type: ProductFilter.Type = ProductFilter.Type.PRICE

    override val isApplied: Boolean get() = min != null || max != null

    override val isEmpty: Boolean get() = false

    override fun coerceInAvailable(available: ProductPriceFilter): ProductPriceFilter {
        return if (this.limits != available.limits) {
            this.copy(
                min = this.min?.coerceIn(available.limits.min, available.limits.max),
                max = this.max?.coerceIn(available.limits.min, available.limits.max),
                limits = available.limits,
            )
        } else this
    }

    public companion object {
        public fun getEmpty(): ProductPriceFilter {
            return ProductPriceFilter(min = null, max = null, limits = PriceRange.getEmpty())
        }
    }
}

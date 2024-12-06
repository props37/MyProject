package ru.livetyping.zarina.core.domain.model.product.filter

public interface ProductFilter<T : ProductFilter<T>> {
    public val type: Type
    public val isApplied: Boolean
    public val isEmpty: Boolean

    public fun coerceInAvailable(available: T): T

    public enum class Type {
        SORTING,
        PRICE,
        MATERIALS,
        SIZES,
        COLORS,
        DELIVERY_AVAILABILITY,
        STORE_PICKUP_AVAILABILITY,
        PICKUP_STORES,
    }
}

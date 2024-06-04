package ru.livetyping.zarina.domain.filter

// TODO: [Low] Rename to ProductFilter?
sealed interface Filter {
    val type: Type
    val isApplied: Boolean
    val isEmpty: Boolean

    enum class Type {
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

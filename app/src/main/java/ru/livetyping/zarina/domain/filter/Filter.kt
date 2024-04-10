package ru.livetyping.zarina.domain.filter

sealed interface Filter {
    val type: Type
    val isEmpty: Boolean

    enum class Type {
        SORTING,
        PRICE,
        MATERIALS,
        SIZES,
        COLORS,
        DELIVERY_AVAILABILITY,
        STORE_PICKUP_AVAILABILITY,
    }
}

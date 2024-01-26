package ru.zarina.zarina.domain.rework.filter

sealed interface Filter {
    val type: Type
    val isEmpty: Boolean

    enum class Type {
        SORTING,
        PRICE,
        MATERIALS,
        SIZES,
        COLORS,
    }
}

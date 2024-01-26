package ru.zarina.zarina.domain.rework.filter

sealed interface Filter {
    val type: Type

    enum class Type {
        SORTING,
        PRICE,
        MATERIALS,
        SIZES,
        COLORS,
    }
}

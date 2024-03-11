package ru.zarina.zarina.domain.common

enum class Sorting {
    NEW,
    POPULAR,
    DISCOUNT,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW;

    companion object {
        fun getDefault(): Sorting = NEW
    }
}

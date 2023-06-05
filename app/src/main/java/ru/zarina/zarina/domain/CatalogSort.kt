package ru.zarina.zarina.domain

enum class CatalogSort {
    DATE, POPULARITY, PRICE, PRICE_REVERSE, DISCOUNT;

    val DEFAULT
        get() = DATE
}

package ru.livetyping.zarina.domain.old

enum class ProductSort {
    DATE_DESCENDING, POPULARITY, PRICE, PRICE_DESCENDING, DISCOUNT;

    companion object {
        val DEFAULT
            get() = DATE_DESCENDING
    }
}

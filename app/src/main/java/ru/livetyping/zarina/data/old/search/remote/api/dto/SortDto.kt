package ru.livetyping.zarina.data.old.search.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.old.ProductSort

@Serializable
enum class SortDto {
    DEFAULT,
    PRICE_ASC,
    PRICE_DESC;

    companion object {
        fun from(sort: ProductSort): SortDto {
            return when (sort) {
                ProductSort.POPULARITY -> DEFAULT
                ProductSort.PRICE -> PRICE_ASC
                ProductSort.PRICE_DESCENDING -> PRICE_DESC
                else -> throw IllegalArgumentException("Unsupported sort: $sort")
            }
        }
    }
}

package ru.livetyping.zarina.data.productsearch.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.common.Sorting

@Serializable
@JvmInline
value class ProductSearchSortingDto(val value: String) {
    companion object {
        fun from(sorting: Sorting): ProductSearchSortingDto {
            val value = when (sorting) {
                Sorting.NEW -> "3_DESC"
                Sorting.POPULAR -> "DEFAULT"
                Sorting.PRICE_LOW_TO_HIGH, Sorting.DISCOUNT -> "PRICE_ASC"
                Sorting.PRICE_HIGH_TO_LOW -> "PRICE_DESC"
            }
            return ProductSearchSortingDto(value)
        }
    }
}

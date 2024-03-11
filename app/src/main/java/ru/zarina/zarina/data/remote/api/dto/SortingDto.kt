package ru.zarina.zarina.data.remote.api.dto

import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.common.Sorting

@Serializable
@JvmInline
value class SortingDto(val value: String) {
    companion object {
        fun from(sorting: Sorting): SortingDto {
            val value = when (sorting) {
                Sorting.NEW -> "-news"
                Sorting.POPULAR -> "popularity"
                Sorting.DISCOUNT -> "discount"
                Sorting.PRICE_LOW_TO_HIGH -> "price"
                Sorting.PRICE_HIGH_TO_LOW -> "-price"
            }
            return SortingDto(value)
        }
    }
}

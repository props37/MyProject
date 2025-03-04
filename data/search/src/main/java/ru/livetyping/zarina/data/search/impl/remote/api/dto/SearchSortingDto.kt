package ru.livetyping.zarina.data.search.impl.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductSorting

@Serializable
@JvmInline
internal value class SearchSortingDto(val value: String) {
    companion object {
        fun from(sorting: ProductSorting): SearchSortingDto {
            val value = when (sorting) {
                ProductSorting.NEW -> "3_DESC"
                ProductSorting.POPULAR -> "DEFAULT"
                ProductSorting.PRICE_LOW_TO_HIGH, ProductSorting.DISCOUNT -> "PRICE_ASC"
                ProductSorting.PRICE_HIGH_TO_LOW -> "PRICE_DESC"
            }
            return SearchSortingDto(value)
        }
    }
}

package ru.livetyping.zarina.data.product.impl.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductSorting

@Serializable
@JvmInline
internal value class SortingDto(val value: String) {
    companion object {
        fun from(sorting: ProductSorting): SortingDto {
            val value = when (sorting) {
                ProductSorting.NEW -> "-news"
                ProductSorting.POPULAR -> "popularity"
                ProductSorting.DISCOUNT -> "discount"
                ProductSorting.PRICE_LOW_TO_HIGH -> "price"
                ProductSorting.PRICE_HIGH_TO_LOW -> "-price"
            }
            return SortingDto(value)
        }
    }
}

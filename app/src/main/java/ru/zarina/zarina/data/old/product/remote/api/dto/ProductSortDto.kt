package ru.zarina.zarina.data.old.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.old.ProductSort

@Serializable
enum class ProductSortDto {

    @SerialName("-news")
    NEWS_DESCENDING,

    @SerialName("popularity")
    POPULARITY,

    @SerialName("price")
    PRICE,

    @SerialName("-price")
    PRICE_DESCENDING,

    @SerialName("discount")
    DISCOUNT;

    companion object {
        fun from(productSort: ProductSort): ProductSortDto {
            return when (productSort) {
                ProductSort.DATE_DESCENDING -> NEWS_DESCENDING
                ProductSort.POPULARITY -> POPULARITY
                ProductSort.PRICE -> PRICE
                ProductSort.PRICE_DESCENDING -> PRICE_DESCENDING
                ProductSort.DISCOUNT -> DISCOUNT
            }
        }
    }
}

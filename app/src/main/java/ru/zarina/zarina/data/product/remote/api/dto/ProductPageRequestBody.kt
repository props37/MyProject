package ru.zarina.zarina.data.product.remote.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductPageRequestBody(
    @SerialName("category_id")
    val categoryId: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("sort")
    val sort: ProductSortDto,
    @SerialName("filters")
    val filters: FiltersDto?,
)

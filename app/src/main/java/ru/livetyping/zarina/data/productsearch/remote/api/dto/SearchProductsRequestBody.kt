package ru.livetyping.zarina.data.productsearch.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchProductsRequestBody(
    @SerialName("query")
    val query: String,

    @SerialName("sort")
    val sort: ProductSearchSortingDto,

    @SerialName("offset")
    val offset: Int,
)

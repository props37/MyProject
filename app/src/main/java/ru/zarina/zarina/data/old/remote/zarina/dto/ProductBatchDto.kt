package ru.zarina.zarina.data.old.remote.zarina.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Product

@Serializable
data class ProductBatchDto(
    @SerialName("articles")
    val articles: List<String>? = null,
    @SerialName("products")
    val products: List<ProductDto>? = null,

    ) {
    fun toDomain(): List<Product> {
        val articleSet = articles?.toSet().orEmpty()
        val relevantProducts = products?.filter { it.id in articleSet }.orEmpty()
        return relevantProducts.mapNotNull { it.toDomain() }
    }
}

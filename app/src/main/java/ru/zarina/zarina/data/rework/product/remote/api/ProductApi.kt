package ru.zarina.zarina.data.rework.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.rework.product.remote.api.dto.ProductsDto
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class ProductApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getProducts(categoryId: Category.Id, page: Int): ProductsDto {
        val body = GetProductsBody(categoryId = categoryId.value)
        return httpClient.get("/api/v1/products") {
            parameter("page", page)
            setJsonBody(body)
        }.body()
    }

    // TODO: [High] Add sort?
    @Serializable
    private data class GetProductsBody(
        @SerialName("category_id")
        val categoryId: Long,
    )
}

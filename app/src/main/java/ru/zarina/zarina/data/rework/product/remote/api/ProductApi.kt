package ru.zarina.zarina.data.rework.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.rework.common.remote.api.dto.SortingDto
import ru.zarina.zarina.data.rework.product.remote.api.dto.FilteredProductsDto
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class ProductApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getFilteredProducts(
        categoryId: Category.Id,
        page: Int,
        sorting: Sorting,
    ): FilteredProductsDto {
        val body = GetFilteredProductsBody(categoryId.value, SortingDto.from(sorting), page)
        return httpClient.post("/api/v1/products") {
            setJsonBody(body)
        }.body()
    }

    @Serializable
    private data class GetFilteredProductsBody(
        @SerialName("category_id")
        val categoryId: Long,

        @SerialName("sort")
        val sort: SortingDto,

        @SerialName("page")
        val page: Int,
    )
}

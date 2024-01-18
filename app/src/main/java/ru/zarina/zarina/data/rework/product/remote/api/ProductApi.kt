package ru.zarina.zarina.data.rework.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.zarina.zarina.data.rework.common.remote.api.dto.SortingDto
import ru.zarina.zarina.data.rework.product.remote.api.dto.FilteredProductsDto
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
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
        return httpClient.get("/api/v1/products") {
            parameter("category_id", categoryId.value)
            parameter("page", page)
            parameter("sort", SortingDto.from(sorting))
        }.body()
    }
}

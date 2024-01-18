package ru.zarina.zarina.data.rework.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.zarina.zarina.data.rework.product.remote.api.dto.ProductsDto
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import javax.inject.Inject

class ProductApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    // TODO: [High] Add sorting
    suspend fun getProducts(categoryId: Category.Id, sorting: Sorting, page: Int): ProductsDto {
        return httpClient.get("/api/v1/products") {
            parameter("category_id", categoryId.value)
            parameter("page", page)
            parameter("sort", getProductsSortingValue(sorting))
        }.body()
    }

    // TODO: [Low] Extract?
    private fun getProductsSortingValue(sorting: Sorting): String = when (sorting) {
        Sorting.NEW -> "-news"
        Sorting.POPULAR -> "popularity"
        Sorting.DISCOUNT -> "discount"
        Sorting.PRICE_LOW_TO_HIGH -> "price"
        Sorting.PRICE_HIGH_TO_LOW -> "-price"
    }
}

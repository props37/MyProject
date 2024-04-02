package ru.zarina.zarina.data.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import ru.zarina.zarina.data.common.remote.api.dto.SortingDto
import ru.zarina.zarina.data.common.remote.api.exception.apiExceptionConverter
import ru.zarina.zarina.data.product.remote.api.dto.FiltersRequestDto
import ru.zarina.zarina.data.product.remote.api.dto.GetProductsRequestBody
import ru.zarina.zarina.data.product.remote.api.dto.ProductsDto
import ru.zarina.zarina.data.product.remote.api.dto.SubscribeToProductRequestBody
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.common.Email
import ru.zarina.zarina.domain.common.Sorting
import ru.zarina.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject
import ru.zarina.zarina.domain.filter.Filters as DomainFilters

class ProductApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getProducts(
        categoryId: Category.Id,
        filters: DomainFilters?,
        sorting: Sorting,
        page: Int,
    ): ProductsDto {
        val body = GetProductsRequestBody(
            categoryId = categoryId.value,
            filters = filters?.let { FiltersRequestDto.from(it) },
            sorting = SortingDto.from(sorting),
            page = page,
        )
        return httpClient.post("/api/v1/products") {
            setJsonBody(body)
        }.body()
    }

    suspend fun getCategoryProductInfo(
        categoryId: Category.Id,
        filters: DomainFilters?,
    ): ProductsDto {
        val body = GetProductsRequestBody(
            categoryId = categoryId.value,
            filters = filters?.let { FiltersRequestDto.from(it) },
            sorting = SortingDto.from(Sorting.getDefault()),
            page = 1,
            returnProducts = false,
        )
        return httpClient.post("/api/v1/products") {
            setJsonBody(body)
        }.body()
    }

    suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email) {
        val body = SubscribeToProductRequestBody(
            barcodes = listOf(barcode.value),
            email = email.value,
            firstName = firstName,
        )
        apiExceptionConverter {
            httpClient.post("/api/subscriptions/subscribe/") {
                setJsonBody(body)
            }
        }
    }
}

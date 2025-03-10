package ru.livetyping.zarina.data.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductDetailsDto
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.ProductItemDto
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.SortingDto
import ru.livetyping.zarina.data.product.remote.api.dto.FiltersRequestDto
import ru.livetyping.zarina.data.product.remote.api.dto.GetProductsRequestBody
import ru.livetyping.zarina.data.product.remote.api.dto.ProductAvailabilityInStoreDto
import ru.livetyping.zarina.data.product.remote.api.dto.ProductsDto
import ru.livetyping.zarina.data.product.remote.api.dto.SubscribeToProductRequestBody
import ru.livetyping.zarina.data.product.remote.api.exception.ProductAvailabilityInStoreApiExceptionConverter
import ru.livetyping.zarina.data.product.remote.api.exception.ProductSuggestionsApiExceptionConverter
import ru.livetyping.zarina.data.product.remote.api.exception.SubscribeToProductApiExceptionConverter
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.geography.KladrId
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class ProductApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val subscribeToProductApiExceptionConverter: SubscribeToProductApiExceptionConverter,
    private val productSuggestionsApiExceptionConverter: ProductSuggestionsApiExceptionConverter,
    private val productAvailabilityInStoreApiExceptionConverter: ProductAvailabilityInStoreApiExceptionConverter,
) {
    suspend fun getProducts(
        categoryId: Category.Id,
        filters: Filters?,
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

    suspend fun getProduct(productId: Product.Id): ProductDetailsDto {
        return httpClient.get("/api/v1/products/${productId.value}").body()
    }

    suspend fun getProductTotalLook(productId: Product.Id): List<ProductItemDto> {
        return productSuggestionsApiExceptionConverter {
            httpClient.get("/api/v1/products/${productId.value}/total_look").body()
        }
    }

    suspend fun getProductSimilar(productId: Product.Id): List<ProductItemDto>{
        return productSuggestionsApiExceptionConverter {
            httpClient.get("api/v1/products/${productId.value}/similar_products").body()
        }
    }

    suspend fun getProductAvailabilityInStores(
        offer: ProductOffer,
        cityKladrId: KladrId,
    ): List<ProductAvailabilityInStoreDto> {
        val barcode = offer.barcode.value
        val kladrId = cityKladrId.value
        return productAvailabilityInStoreApiExceptionConverter {
            httpClient.get("/api/products/stock/offers/$barcode/city/$kladrId").body()
        }
    }

    suspend fun getCategoryProductInfo(
        categoryId: Category.Id,
        filters: Filters?,
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
        subscribeToProductApiExceptionConverter {
            httpClient.post("/api/subscriptions/subscribe/") {
                setJsonBody(body)
            }
        }
    }
}

package ru.livetyping.zarina.data.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.network.di.DigineticaApi
import ru.livetyping.zarina.core.network.di.DigineticaApiType
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.util.setJsonBody
import ru.livetyping.zarina.core.network.zarina.dto.ProductShortDto
import ru.livetyping.zarina.data.product.remote.api.dto.FiltersRequestDto
import ru.livetyping.zarina.data.product.remote.api.dto.GetProductsRequestBody
// ========== REVIEW FROM HERE ==========
import ru.livetyping.zarina.data.product.remote.api.dto.ProductAiReviewDto
import ru.livetyping.zarina.data.product.remote.api.dto.ProductAiReviewRequestBody
// ========== TO HERE, AND ==========
import ru.livetyping.zarina.data.product.remote.api.dto.ProductAvailabilityInStoreDto
import ru.livetyping.zarina.data.product.remote.api.dto.ProductDetailedDto
import ru.livetyping.zarina.data.product.remote.api.dto.ProductsDto
import ru.livetyping.zarina.data.product.remote.api.dto.SortingDto
import ru.livetyping.zarina.data.product.remote.api.dto.SubscribeToProductRequestBody
import ru.livetyping.zarina.data.product.remote.api.exception.ProductAvailabilityInStoreApiExceptionConverter
import ru.livetyping.zarina.data.product.remote.api.exception.ProductSuggestionsApiExceptionConverter
import ru.livetyping.zarina.data.product.remote.api.exception.SubscribeToProductApiExceptionConverter
import javax.inject.Inject

internal class ProductApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val zarinaHttpClient: HttpClient,
    @DigineticaApi(DigineticaApiType.REVIEW)
    private val digineticaReviewHttpClient: HttpClient,
    private val subscribeToProductApiExceptionConverter: SubscribeToProductApiExceptionConverter,
    private val productSuggestionsApiExceptionConverter: ProductSuggestionsApiExceptionConverter,
    private val productAvailabilityInStoreApiExceptionConverter: ProductAvailabilityInStoreApiExceptionConverter,
) : ProductApi {

    override suspend fun getProducts(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        page: Int,
        pageSize: Int,
    ): ProductsDto {
        val body = GetProductsRequestBody(
            categoryId = categoryId.value,
            filters = filters?.let { FiltersRequestDto.from(it) },
            sorting = SortingDto.from(sorting),
            page = page,
            pageSize = pageSize,
        )
        return zarinaHttpClient.post("/api/v1/products") {
            setJsonBody(body)
        }.body()
    }

    override suspend fun getProduct(productId: Product.Id): ProductDetailedDto {
        return zarinaHttpClient.get("/api/v1/products/${productId.value}").body()
    }

    override suspend fun getProductTotalLook(productId: Product.Id): List<ProductShortDto> {
        return productSuggestionsApiExceptionConverter {
            zarinaHttpClient.get("/api/v1/products/${productId.value}/total_look").body()
        }
    }

    override suspend fun getSimilarProducts(productId: Product.Id): List<ProductShortDto> {
        return productSuggestionsApiExceptionConverter {
            zarinaHttpClient.get("api/v1/products/${productId.value}/similar_products").body()
        }
    }

    override suspend fun getProductAvailabilityInStores(
        offer: ProductOffer,
        cityFiasId: FiasId,
    ): List<ProductAvailabilityInStoreDto> {
        val barcode = offer.barcode.value
        val fiasId = cityFiasId.value
        return productAvailabilityInStoreApiExceptionConverter {
            zarinaHttpClient.get("/api/products/stock/offers/$barcode/city/$fiasId").body()
        }
    }

    override suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email) {
        val body = SubscribeToProductRequestBody(
            barcodes = listOf(barcode.value),
            email = email.value,
            firstName = firstName,
        )
        subscribeToProductApiExceptionConverter {
            zarinaHttpClient.post("/api/subscriptions/subscribe/") {
                setJsonBody(body)
            }
        }
    }

    override suspend fun getCategoryInfo(
        categoryId: Category.Id,
        filters: ProductFilters?,
    ): ProductsDto {
        val body = GetProductsRequestBody(
            categoryId = categoryId.value,
            filters = filters?.let { FiltersRequestDto.from(it) },
            sorting = SortingDto.from(ProductSorting.getDefault()),
            page = 1,
            pageSize = 10,
            returnProducts = false,
        )
        return zarinaHttpClient.post("/api/v1/products") {
            setJsonBody(body)
        }.body()
    }
    // ========== REVIEW FROM HERE ==========
    override suspend fun getProductAiReviews(groupId: Product.GroupId): List<ProductAiReviewDto> {
        val body = ProductAiReviewRequestBody(
            externalIds = listOf(groupId.value)
        )

        return digineticaReviewHttpClient.post("/v1/micro-reviews/reviews/get") {
            setJsonBody(body)
        }.body()
    }
    // ========== TO HERE ==========
}

package ru.zarina.zarina.data.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductBatchDto
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductDto
import ru.zarina.zarina.data.common.remote.zarina.dto.SizeDto
import ru.zarina.zarina.data.product.remote.api.dto.DeliveryInfoDto
import ru.zarina.zarina.data.product.remote.api.dto.FiltersRequestDto
import ru.zarina.zarina.data.product.remote.api.dto.ProductPageRequestBody
import ru.zarina.zarina.data.product.remote.api.dto.ProductPageResponseDto
import ru.zarina.zarina.data.product.remote.api.dto.ProductSortDto
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.exception.NotFoundException

@Factory
class KtorZarinaProductApi(
    @Named(Qualifiers.Authorization.TOKEN)
    private val client: HttpClient,
) : IZarinaProductApi {

    override suspend fun getProduct(id: String): ProductDto {
        try {
            val response = client.get("/api/products/$id")
            return response.body()
        } catch (exception: ClientRequestException) {
            if (exception.response.status == HttpStatusCode.NotFound)
                throw NotFoundException("Product $id not found")
            else
                throw exception
        }
    }

    override suspend fun getProductPage(
        categoryId: Int,
        sort: ProductSortDto,
        filters: FiltersRequestDto?,
        pageIndex: Int,
    ): ProductPageResponseDto {
        val body = ProductPageRequestBody(
            categoryId = categoryId,
            sort = sort,
            filters = filters,
            page = pageIndex,
        )
        val response = client.post("/api/products") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body()
    }

    override suspend fun getCompleteLook(id: String): ProductBatchDto {
        val response = client.get("/api/products/$id/total_look") {
            parameter("with-articles", "")
        }
        return response.body()
    }

    override suspend fun getDeliveryInfo(id: String): DeliveryInfoDto {
        val response = client.get("/api/products/$id/delivery-info")
        return response.body()
    }

    override suspend fun getSizes(productId: String, cityId: String): List<SizeDto> {
        val response = client.get("/api/products/stock/$productId/city/$cityId")
        return response.body()
    }
}

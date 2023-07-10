package ru.zarina.zarina.data.product.remote.api

import ru.zarina.zarina.data.common.remote.zarina.dto.ProductBatchDto
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductDto
import ru.zarina.zarina.data.common.remote.zarina.dto.SizeDto
import ru.zarina.zarina.data.product.remote.api.dto.DeliveryInfoDto
import ru.zarina.zarina.data.product.remote.api.dto.FiltersRequestDto
import ru.zarina.zarina.data.product.remote.api.dto.ProductPageResponseDto
import ru.zarina.zarina.data.product.remote.api.dto.ProductSortDto

interface IZarinaProductApi {
    suspend fun getProduct(id: String): ProductDto
    suspend fun getProductPage(
        categoryId: Int,
        sort: ProductSortDto,
        filters: FiltersRequestDto?,
        pageIndex: Int,
    ): ProductPageResponseDto

    suspend fun getCompleteLook(id: String): ProductBatchDto
    suspend fun getDeliveryInfo(id: String): DeliveryInfoDto
    suspend fun getSizes(productId: String, cityId: String): List<SizeDto>
}

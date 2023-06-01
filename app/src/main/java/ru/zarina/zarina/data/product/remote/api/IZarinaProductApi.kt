package ru.zarina.zarina.data.product.remote.api

import ru.zarina.zarina.data.common.remote.zarina.dto.ProductBatchDto
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductDto
import ru.zarina.zarina.data.common.remote.zarina.dto.SizeDto
import ru.zarina.zarina.data.product.remote.api.dto.DeliveryInfoDto
import ru.zarina.zarina.data.product.remote.api.dto.ProductPageResponseDto

interface IZarinaProductApi {
    suspend fun getProduct(id: String): ProductDto
    suspend fun getProductPage(categoryId: Int, pageIndex: Int): ProductPageResponseDto
    suspend fun getCompleteLook(id: String): ProductBatchDto
    suspend fun getDeliveryInfo(id: String): DeliveryInfoDto
    suspend fun getSizes(productId: String, cityId: String): List<SizeDto>
}

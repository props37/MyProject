package ru.livetyping.zarina.data.old.product.remote.api

import ru.livetyping.zarina.data.old.product.remote.api.dto.DeliveryInfoDto
import ru.livetyping.zarina.data.old.product.remote.api.dto.FiltersRequestDto
import ru.livetyping.zarina.data.old.product.remote.api.dto.ProductPageResponseDto
import ru.livetyping.zarina.data.old.product.remote.api.dto.ProductSortDto
import ru.livetyping.zarina.data.old.remote.zarina.dto.ProductBatchDto
import ru.livetyping.zarina.data.old.remote.zarina.dto.ProductDto
import ru.livetyping.zarina.data.old.remote.zarina.dto.SizeDto

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

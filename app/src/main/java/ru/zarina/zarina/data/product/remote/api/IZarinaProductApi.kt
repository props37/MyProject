package ru.zarina.zarina.data.product.remote.api

import ru.zarina.zarina.data.product.remote.api.dto.CompleteLookDto
import ru.zarina.zarina.data.product.remote.api.dto.DeliveryInfoDto
import ru.zarina.zarina.data.product.remote.api.dto.ProductDto

interface IZarinaProductApi {
    suspend fun getProduct(id: String): ProductDto
    suspend fun getCompleteLook(id: String): CompleteLookDto
    suspend fun getDeliveryInfo(id: String): DeliveryInfoDto
}

package ru.zarina.zarina.data.product.remote.api

import ru.zarina.zarina.data.product.remote.api.dto.ProductDto

interface IZarinaProductApi {
    suspend fun getProduct(id: String): ProductDto
}

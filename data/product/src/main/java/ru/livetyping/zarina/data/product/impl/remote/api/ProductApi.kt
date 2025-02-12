package ru.livetyping.zarina.data.product.impl.remote.api

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.network.zarina.dto.ProductShortDto
import ru.livetyping.zarina.data.product.impl.remote.api.dto.ProductDetailedDto
import ru.livetyping.zarina.data.product.impl.remote.api.dto.ProductsDto

internal interface ProductApi {
    suspend fun getProducts(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        page: Int,
    ): ProductsDto

    suspend fun getProduct(productId: Product.Id): ProductDetailedDto

    suspend fun getProductTotalLook(productId: Product.Id): List<ProductShortDto>

    suspend fun getSimilarProducts(productId: Product.Id): List<ProductShortDto>

    suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email)

    suspend fun getCategoryInfo(categoryId: Category.Id, filters: ProductFilters?): ProductsDto
}

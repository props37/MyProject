package ru.livetyping.zarina.data.product.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters

internal interface ProductRemoteDataSource {
    fun getProductsWithFiltersPageFlow(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        page: Int,
    ): Flow<Page<ProductsWithFilters>>

    suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email)
}

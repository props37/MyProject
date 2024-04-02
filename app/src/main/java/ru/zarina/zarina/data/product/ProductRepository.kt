package ru.zarina.zarina.data.product

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.product.remote.ProductRemoteDataSource
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.common.Email
import ru.zarina.zarina.domain.common.Page
import ru.zarina.zarina.domain.common.Sorting
import ru.zarina.zarina.domain.filter.Filters
import ru.zarina.zarina.domain.product.CategoryProductInfo
import ru.zarina.zarina.domain.product.ProductsWithFilters
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource,
) {
    fun getProductsWithFiltersPageFlow(
        categoryId: Category.Id,
        filters: Filters?,
        sorting: Sorting,
        page: Int,
    ): Flow<Page<ProductsWithFilters>> {
        return remoteDataSource.getProductsWithFiltersPageFlow(
            categoryId = categoryId,
            filters = filters,
            sorting = sorting,
            page = page,
        )
    }

    fun getCategoryProductInfoFlow(
        categoryId: Category.Id,
        filters: Filters?,
    ): Flow<CategoryProductInfo> {
        return remoteDataSource.getCategoryProductInfoFlow(
            categoryId = categoryId,
            filters = filters,
        )
    }

    suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email) {
        remoteDataSource.subscribeToProduct(barcode, firstName, email)
    }
}

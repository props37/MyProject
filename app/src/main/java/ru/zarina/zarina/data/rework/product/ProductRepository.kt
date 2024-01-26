package ru.zarina.zarina.data.rework.product

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.product.remote.ProductRemoteDataSource
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Page
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.domain.rework.filter.Filters
import ru.zarina.zarina.domain.rework.product.CategoryProductInfo
import ru.zarina.zarina.domain.rework.product.ProductsWithFilters
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
}

package ru.livetyping.zarina.data.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.data.product.remote.ProductRemoteDataSource
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.CategoryProductInfo
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.domain.product.ProductsWithFilters
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

    fun getProductFlow(productId: Product.Id): Flow<ProductDetails> {
        return remoteDataSource.getProductFlow(productId)
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

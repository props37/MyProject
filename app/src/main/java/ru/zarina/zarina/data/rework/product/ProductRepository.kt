package ru.zarina.zarina.data.rework.product

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.product.remote.ProductRemoteDataSource
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Page
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource,
) {
    fun getProductPageFlow(
        categoryId: Category.Id,
        page: Int,
        sorting: Sorting,
    ): Flow<Page<List<Product>>> {
        return remoteDataSource.getProductPageFlow(categoryId, page, sorting)
    }
}

package ru.zarina.zarina.data.rework.product.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.zarina.zarina.data.rework.product.remote.api.ProductApi
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Page
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.domain.rework.product.CategoryProductInfo
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val api: ProductApi,
) {
    fun getProductPageFlow(
        categoryId: Category.Id,
        page: Int,
        sorting: Sorting,
    ): Flow<Page<List<Product>>> = flow {
        val productPage = api.getProducts(categoryId, page, sorting).toProductPage()
        emit(productPage)
    }

    fun getCategoryProductInfoFlow(categoryId: Category.Id): Flow<CategoryProductInfo> = flow {
        val categoryProductInfo = api.getProducts(
            categoryId = categoryId,
            page = 1,
            sorting = Sorting.getDefault(),
        ).toCategoryProductInfo(categoryId)
        emit(categoryProductInfo)
    }
}

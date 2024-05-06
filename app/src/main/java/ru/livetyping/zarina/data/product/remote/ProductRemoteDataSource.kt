package ru.livetyping.zarina.data.product.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.data.product.remote.api.ProductApi
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.CategoryProductInfo
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.domain.product.ProductsWithFilters
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val api: ProductApi,
) {
    fun getProductsWithFiltersPageFlow(
        categoryId: Category.Id,
        filters: Filters?,
        sorting: Sorting,
        page: Int,
    ): Flow<Page<ProductsWithFilters>> = flow {
        val productsWithFiltersPage = api.getProducts(
            categoryId = categoryId,
            filters = filters,
            sorting = sorting,
            page = page,
        ).toProductsWithFiltersPage()
        emit(productsWithFiltersPage)
    }

    fun getProductFlow(productId: Product.Id): Flow<ProductDetails> = flow {
        val product = api.getProduct(productId).toProductDetails()
        emit(product)
    }

    fun getProductTotalLookFlow(productId: Product.Id): Flow<List<ProductItem>> = flow {
        val totalLook = api.getProductTotalLook(productId).mapNotNull { it.toProductItem() }
        emit(totalLook)
    }

    fun getCategoryProductInfoFlow(
        categoryId: Category.Id,
        filters: Filters?,
    ): Flow<CategoryProductInfo> = flow {
        val categoryProductInfo = api.getCategoryProductInfo(
            categoryId = categoryId,
            filters = filters,
        ).toCategoryProductInfo(categoryId)
        emit(categoryProductInfo)
    }

    suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email) {
        api.subscribeToProduct(barcode, firstName, email)
    }
}

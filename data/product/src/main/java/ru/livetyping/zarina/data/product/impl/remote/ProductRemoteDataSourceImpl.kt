package ru.livetyping.zarina.data.product.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters
import ru.livetyping.zarina.data.product.impl.remote.api.ProductApi
import javax.inject.Inject

internal class ProductRemoteDataSourceImpl @Inject constructor(
    private val api: ProductApi,
) : ProductRemoteDataSource {
    override fun getProductsWithFiltersPageFlow(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        page: Int
    ): Flow<Page<ProductsWithFilters>> = flow {
        val productsWithFiltersPage = api.getProducts(
            categoryId = categoryId,
            filters = filters,
            sorting = sorting,
            page = page,
        ).toProductsWithFiltersPage()
        emit(productsWithFiltersPage)
    }

    override suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email) {
        api.subscribeToProduct(barcode, firstName, email)
    }
}

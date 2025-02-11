package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters

public interface ProductRepository {
    public fun getProductsWithFiltersPageFlow(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        page: Int,
    ): Flow<Page<ProductsWithFilters>>

    public fun getProductFlow(productId: Product.Id): Flow<ProductDetailed>

    public fun getProductTotalLookFlow(productId: Product.Id): Flow<List<ProductShort>>

    public fun getSimilarProductsFlow(productId: Product.Id): Flow<List<ProductShort>>

    public suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email)
}

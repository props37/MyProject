package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryInfo
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
// ========== REVIEW FROM HERE ==========
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
// ========== TO HERE, AND ==========
import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters

public interface ProductRepository {
    public suspend fun getProductsWithFiltersPage(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        page: Int,
        pageSize: Int,
    ): Page<ProductsWithFilters>

    public suspend fun getProduct(productId: Product.Id): ProductDetailed

    public suspend fun getProductTotalLook(productId: Product.Id): List<ProductShort>

    public suspend fun getSimilarProducts(productId: Product.Id): List<ProductShort>

    public fun getProductAvailabilityInStoresFlow(
        offer: ProductOffer,
        cityFiasId: FiasId,
    ): Flow<List<ProductAvailabilityInStore>>

    public suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email)

    public fun getCategoryInfoFlow(
        categoryId: Category.Id,
        filters: ProductFilters?,
    ): Flow<CategoryInfo>
    // ========== REVIEW FROM HERE ==========
    public suspend fun getProductAiReviews(groupId: Product.GroupId): ProductAiReviews
    // ========== TO HERE ==========
}
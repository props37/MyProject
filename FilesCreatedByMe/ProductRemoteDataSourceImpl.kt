package ru.livetyping.zarina.data.product.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
import ru.livetyping.zarina.data.product.remote.api.ProductApi
import javax.inject.Inject

internal class ProductRemoteDataSourceImpl @Inject constructor(
    private val api: ProductApi,
) : ProductRemoteDataSource {
    override suspend fun getProductsWithFiltersPage(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        page: Int,
        pageSize: Int,
    ): Page<ProductsWithFilters> {
        val productsWithFiltersPage = api.getProducts(
            categoryId = categoryId,
            filters = filters,
            sorting = sorting,
            page = page,
            pageSize = pageSize,
        ).toProductsWithFiltersPage()
        return productsWithFiltersPage
    }

    override suspend fun getProduct(productId: Product.Id): ProductDetailed {
        return api.getProduct(productId).toProductDetailed()
    }

    override suspend fun getProductTotalLook(productId: Product.Id): List<ProductShort> {
        return api.getProductTotalLook(productId)
            .mapNotNull { it.toProductShort() }
            .distinctBy { it.id }
    }

    override suspend fun getSimilarProducts(productId: Product.Id): List<ProductShort> {
        return api.getSimilarProducts(productId)
            .mapNotNull { it.toProductShort() }
            .distinctBy { it.id }
    }

    override fun getProductAvailabilityInStoresFlow(
        offer: ProductOffer,
        cityFiasId: FiasId
    ): Flow<List<ProductAvailabilityInStore>> = flow {
        val dto = api.getProductAvailabilityInStores(offer, cityFiasId)
        val availabilityList = dto.map { it.toProductAvailabilityInStore() }
        emit(availabilityList)
    }

    override suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email) {
        api.subscribeToProduct(barcode, firstName, email)
    }

    override fun getCategoryInfoFlow(
        categoryId: Category.Id,
        filters: ProductFilters?,
    ): Flow<CategoryInfo> = flow {
        val categoryInfo = api.getCategoryInfo(categoryId, filters).toCategoryInfo(categoryId)
        emit(categoryInfo)
    }
    // ========== REVIEW FROM HERE ==========
    override suspend fun getProductAiReviews(groupId: Product.GroupId): ProductAiReviews {
        val dtoList = api.getProductAiReviews(groupId)
        val dto = dtoList.firstOrNull()
        val productAiReviews = dto?.toProductAiReviews()
        return checkNotNull(productAiReviews) { "productAiReviews not found" }
    }
    // ========== TO HERE ==========
}
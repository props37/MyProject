package ru.livetyping.zarina.data.product.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.CategoryInfo
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.product.filter.ProductsWithFilters
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.data.product.impl.remote.ProductRemoteDataSource
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource,
) : ProductRepository {
    override suspend fun getProductsWithFiltersPage(
        categoryId: Category.Id,
        filters: ProductFilters?,
        sorting: ProductSorting,
        page: Int,
        pageSize: Int,
    ): Page<ProductsWithFilters> {
        return remoteDataSource.getProductsWithFiltersPage(
            categoryId = categoryId,
            filters = filters,
            sorting = sorting,
            page = page,
            pageSize = pageSize,
        )
    }

    override suspend fun getProduct(productId: Product.Id): ProductDetailed {
        return remoteDataSource.getProduct(productId)
    }

    override suspend fun getProductTotalLook(productId: Product.Id): List<ProductShort> {
        return remoteDataSource.getProductTotalLook(productId)
    }

    override suspend fun getSimilarProducts(productId: Product.Id): List<ProductShort> {
        return remoteDataSource.getSimilarProducts(productId)
    }

    override fun getProductAvailabilityInStoresFlow(
        offer: ProductOffer,
        cityKladrId: KladrId
    ): Flow<List<ProductAvailabilityInStore>> {
        return remoteDataSource.getProductAvailabilityInStoresFlow(offer, cityKladrId)
    }

    override suspend fun subscribeToProduct(barcode: Barcode, firstName: String, email: Email) {
        remoteDataSource.subscribeToProduct(barcode, firstName, email)
    }

    override fun getCategoryInfoFlow(
        categoryId: Category.Id,
        filters: ProductFilters?,
    ): Flow<CategoryInfo> {
        return remoteDataSource.getCategoryInfoFlow(categoryId, filters)
    }
}

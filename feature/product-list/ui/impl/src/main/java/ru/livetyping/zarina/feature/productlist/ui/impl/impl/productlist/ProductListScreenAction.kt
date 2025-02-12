package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

internal sealed interface ProductListScreenAction {
    data object BackClicked : ProductListScreenAction

    data class FiltersClicked(
        val categoryId: Category.Id,
        val filters: ProductFilters?,
    ) : ProductListScreenAction

    data class TagClicked(
        val tag: Category,
        val filters: ProductFilters?,
    ) : ProductListScreenAction

    data class ProductClicked(val product: Product) : ProductListScreenAction

    data class SubscribeToProductClicked(
        val product: Product,
        val offer: ProductOffer,
    ) : ProductListScreenAction
}

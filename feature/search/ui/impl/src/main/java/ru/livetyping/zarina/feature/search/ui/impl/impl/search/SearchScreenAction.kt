package ru.livetyping.zarina.feature.search.ui.impl.impl.search

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

internal sealed interface SearchScreenAction {
    data object BackClicked : SearchScreenAction

    data class FiltersClicked(
        val searchQuery: String,
        val filters: ProductFilters?,
    ) : SearchScreenAction

    data class CategoryClicked(val categoryId: Category.Id) : SearchScreenAction

    data class ProductClicked(val product: Product) : SearchScreenAction

    data class SubscribeToProductClicked(
        val product: Product,
        val offer: ProductOffer,
    ) : SearchScreenAction
}

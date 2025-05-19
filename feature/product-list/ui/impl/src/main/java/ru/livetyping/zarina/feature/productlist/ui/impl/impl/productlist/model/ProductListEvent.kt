package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist.model

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface ProductListEvent {
    data object BackClicked : ProductListEvent

    data object SearchClicked : ProductListEvent

    data object FiltersClicked : ProductListEvent

    data class SubcategoryClicked(val category: Category) : ProductListEvent

    data class ProductClicked(val product: Product) : ProductListEvent

    data class AddToWishlistClicked(val product: Product) : ProductListEvent

    data object PullRefreshTriggered : ProductListEvent

    data object RefreshClicked : ProductListEvent

    data object SystemBackClicked : ProductListEvent
}

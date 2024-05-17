package ru.livetyping.zarina.presentation.screen.products

import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.Product

sealed class ProductsScreenAction {
    data object ScreenClosed : ProductsScreenAction()

    data object SearchClicked : ProductsScreenAction()

    data class FiltersClicked(
        val categoryId: Category.Id,
        val filters: Filters?,
    ) : ProductsScreenAction()

    data class TagClicked(
        val tag: Category,
        val filters: Filters?,
    ) : ProductsScreenAction()

    data class AddProductToCartClicked(val product: Product) : ProductsScreenAction()

    data class SubscribeToProductClicked(val product: Product) : ProductsScreenAction()

    data class ProductClicked(val product: Product) : ProductsScreenAction()
}

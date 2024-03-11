package ru.zarina.zarina.ui.screen.products

import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.filter.Filters
import ru.zarina.zarina.domain.rework.product.Product

sealed class ProductsScreenAction {
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
}

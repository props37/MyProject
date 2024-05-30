package ru.livetyping.zarina.presentation.screen.productsearch

import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.product.Product

sealed class ProductSearchScreenAction {
    data object ScreenClosed : ProductSearchScreenAction()

    data class CategoryClicked(val categoryId: Category.Id) : ProductSearchScreenAction()

    data class ProductClicked(val product: Product) : ProductSearchScreenAction()

    data class AddProductToCartClicked(val product: Product) : ProductSearchScreenAction()

    data class SubscribeToProductClicked(val product: Product) : ProductSearchScreenAction()
}

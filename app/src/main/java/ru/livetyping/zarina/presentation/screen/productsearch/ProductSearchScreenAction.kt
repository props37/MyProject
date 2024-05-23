package ru.livetyping.zarina.presentation.screen.productsearch

import ru.livetyping.zarina.domain.category.Category

sealed class ProductSearchScreenAction {
    data object ScreenClosed : ProductSearchScreenAction()

    data class CategoryClicked(val categoryId: Category.Id) : ProductSearchScreenAction()
}

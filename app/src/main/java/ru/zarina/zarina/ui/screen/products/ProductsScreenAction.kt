package ru.zarina.zarina.ui.screen.products

import ru.zarina.zarina.domain.rework.category.Category

sealed class ProductsScreenAction {
    data class FiltersClicked(val categoryId: Category.Id) : ProductsScreenAction()
}

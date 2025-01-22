package ru.livetyping.zarina.domain.common

import ru.livetyping.zarina.domain.category.Category

sealed interface ClickAction {
    data class ProductList(val categoryId: Category.Id) : ClickAction
}

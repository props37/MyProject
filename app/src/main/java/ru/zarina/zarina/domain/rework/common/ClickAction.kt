package ru.zarina.zarina.domain.rework.common

import ru.zarina.zarina.domain.category.Category

sealed interface ClickAction {
    data class Products(val categoryId: Category.Id) : ClickAction
}

package ru.zarina.zarina.domain.rework.common

sealed interface ClickAction {
    data class Products(val categoryId: Category.Id) : ClickAction
}

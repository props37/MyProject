package ru.zarina.zarina.domain

sealed interface Action {
    data class Product(val id: ru.zarina.zarina.domain.Product.Id) : Action
    data class Products(val categoryId: Category.Id, val filtration: Filtration?) : Action
    data class Link(val url: Url) : Action
}
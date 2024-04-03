package ru.livetyping.zarina.domain.old

sealed interface Action {
    data class Product(val id: ru.livetyping.zarina.domain.old.Product.Id) : Action
    data class Products(val categoryId: Category.Id, val filtration: Filtration?) : Action
    data class Link(val url: Url) : Action
}

package ru.zarina.zarina.domain

data class Category(
    val id: Int,
    val image: Url?,
    val name: String,
    val subcategories: List<Category>,
)

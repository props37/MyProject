package ru.zarina.zarina.domain.old

data class Category(
    val id: Id,
    val image: Url?,
    val name: String,
    val subcategories: List<Category>,
) {

    fun getFlattenedSubcategories(): List<Category> {
        return subcategories + subcategories.flatMap { it.getFlattenedSubcategories() }
    }

    @JvmInline
    value class Id(val value: Int)

}

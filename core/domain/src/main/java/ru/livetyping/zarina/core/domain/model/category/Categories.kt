package ru.livetyping.zarina.core.domain.model.category

public data class Categories(
    val women: List<Category>,
    val men: List<Category>,
)

public fun Categories.find(predicate: (Category) -> Boolean): Category? {
    val allWomenCategories = women.flatMap { it.withFlattenedChildren() }
    return allWomenCategories.find(predicate) ?: run {
        val allMenCategories = men.flatMap { it.withFlattenedChildren() }
        allMenCategories.find(predicate)
    }
}

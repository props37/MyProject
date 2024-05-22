package ru.livetyping.zarina.domain.productsearch

import ru.livetyping.zarina.domain.category.Category as DomainCategory

// TODO: [High] Rename
data class ProductSearchSuggestions(
    val autocompleteSuggestions: List<AutocompleteSuggestion>,
    val resultSuggestions: List<String>,
    val categories: List<Category>,
) {
    data class AutocompleteSuggestion(
        val text: String,
        val resultQuery: String,
    )

    data class Category(
        val id: DomainCategory.Id,
        val name: String,
    )
}

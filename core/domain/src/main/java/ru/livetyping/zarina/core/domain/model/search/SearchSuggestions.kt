package ru.livetyping.zarina.core.domain.model.search

import ru.livetyping.zarina.core.domain.model.category.Category as DomainCategory

// Marked as stable on config/compose/stability_config.txt
public data class SearchSuggestions(
    val autocompleteSuggestions: List<AutocompleteSuggestion>,
    val querySuggestions: List<String>,
    val categories: List<Category>,
) {
    // Marked as stable on config/compose/stability_config.txt
    public data class AutocompleteSuggestion(
        val text: String,
        val resultQuery: String,
    )

    // Marked as stable on config/compose/stability_config.txt
    public data class Category(
        val id: DomainCategory.Id,
        val name: String,
    )
}

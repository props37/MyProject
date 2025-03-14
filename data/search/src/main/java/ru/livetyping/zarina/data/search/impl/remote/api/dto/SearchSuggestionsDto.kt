package ru.livetyping.zarina.data.search.impl.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import timber.log.Timber

@Serializable
internal data class SearchSuggestionsDto(
    @SerialName("taps")
    val taps: List<AutocompleteSuggestionDto>? = null,

    @SerialName("sts")
    val sts: List<QuerySuggestionDto>? = null,

    @SerialName("categories")
    val categories: List<CategoryDto>? = null,
) {
    fun toSearchSuggestions(): SearchSuggestions {
        val autocompleteSuggestions = taps
            ?.mapNotNull { it.toAutocompleteSuggestion() }
            ?: emptyList()
        val querySuggestions = sts
            ?.mapNotNull { it.st }
            ?: emptyList()
        val categories = categories
            ?.mapNotNull { it.toCategory() }
            ?: emptyList()
        return SearchSuggestions(
            autocompleteSuggestions = autocompleteSuggestions,
            querySuggestions = querySuggestions,
            categories = categories,
        )
    }

    @Serializable
    data class AutocompleteSuggestionDto(
        @SerialName("tap")
        val tap: String? = null,

        @SerialName("relatedSearch")
        val relatedSearch: String? = null,
    ) {
        fun toAutocompleteSuggestion(): SearchSuggestions.AutocompleteSuggestion? {
            return if (tap != null && relatedSearch != null) {
                SearchSuggestions.AutocompleteSuggestion(
                    text = tap,
                    resultQuery = relatedSearch,
                )
            } else {
                Timber.e("Drop AutocompleteSuggestion $this because its text or resultQuery is null")
                null
            }
        }
    }

    @Serializable
    data class QuerySuggestionDto(
        @SerialName("st")
        val st: String? = null,
    )

    @Serializable
    data class CategoryDto(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,
    ) {
        fun toCategory(): SearchSuggestions.Category? {
            return if (id != null && name != null) {
                SearchSuggestions.Category(
                    id = Category.Id(id),
                    name = name,
                )
            } else {
                Timber.e("Drop Category $this because its ID or name is null")
                null
            }
        }
    }
}

package ru.livetyping.zarina.data.productsearch.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.domain.productsearch.ProductSearchSuggestions
import timber.log.Timber
import ru.livetyping.zarina.domain.category.Category as DomainCategory

@Serializable
data class SearchSuggestionsDto(
    @SerialName("taps")
    val autocompleteSuggestions: List<AutocompleteSuggestion>? = null,

    @SerialName("sts")
    val resultSuggestions: List<ResultSuggestion>? = null,

    @SerialName("categories")
    val categories: List<Category>? = null,
) {
    fun toProductSearchSuggestions(): ProductSearchSuggestions {
        val autocompleteSuggestions = autocompleteSuggestions
            ?.mapNotNull { it.toAutocompleteSuggestion() }
            ?: emptyList()
        val resultSuggestions = resultSuggestions?.mapNotNull { it.st } ?: emptyList()
        val categories = categories?.mapNotNull { it.toCategory() } ?: emptyList()
        return ProductSearchSuggestions(
            autocompleteSuggestions = autocompleteSuggestions,
            resultSuggestions = resultSuggestions,
            categories = categories,
        )
    }

    @Serializable
    data class AutocompleteSuggestion(
        @SerialName("tap")
        val text: String? = null,

        @SerialName("relatedSearch")
        val resultQuery: String? = null,
    ) {
        fun toAutocompleteSuggestion(): ProductSearchSuggestions.AutocompleteSuggestion? {
            return if (text != null && resultQuery != null) {
                ProductSearchSuggestions.AutocompleteSuggestion(
                    text = text,
                    resultQuery = resultQuery,
                )
            } else {
                Timber.e("Drop AutocompleteSuggestion because its text or resultQuery is null")
                null
            }
        }
    }

    @Serializable
    data class ResultSuggestion(
        @SerialName("st")
        val st: String? = null,
    )

    @Serializable
    data class Category(
        @SerialName("id")
        val id: String? = null,

        @SerialName("name")
        val name: String? = null,
    ) {
        fun toCategory(): ProductSearchSuggestions.Category? {
            val parsedId = id?.toLongOrNull()
            return if (parsedId != null && name != null) {
                ProductSearchSuggestions.Category(
                    id = DomainCategory.Id(parsedId),
                    name = name,
                )
            } else {
                Timber.e("Drop Category because its ID or name is null")
                null
            }
        }
    }
}

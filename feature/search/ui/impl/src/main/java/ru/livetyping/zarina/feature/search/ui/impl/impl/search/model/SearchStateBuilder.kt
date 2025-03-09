package ru.livetyping.zarina.feature.search.ui.impl.impl.search.model

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenButtonState
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.resource.R as RCommon

internal class SearchStateBuilder {
    private val categoryParentCategoryChainRegex = CATEGORY_PARENT_CATEGORY_CHAIN_PATTERN.toRegex()

    fun build(
        searchHistoryQueriesResult: Result<List<SearchHistoryQuery>>,
        searchSuggestionsResult: Result<SearchSuggestions>,
        query: String,
    ): SearchState {
        return searchSuggestionsResult.fold(
            onSuccess = { suggestions ->
                val autocompleteSuggestions = suggestions.autocompleteSuggestions.toImmutableList()
                val searchSuggestionItems = suggestions.toSearchSuggestionItems()
                val suggestionState = if (searchSuggestionItems.isNotEmpty()) {
                    val searchHistoryQueryItems =
                        searchHistoryQueriesResult.getOrNull()?.toSearchSuggestionItems()
                    val items = if (searchHistoryQueryItems != null) {
                        searchHistoryQueryItems + searchSuggestionItems
                    } else {
                        searchSuggestionItems
                    }
                    SearchState.SuggestionState.Success(items.toImmutableList())
                } else {
                    SearchState.SuggestionState.Empty
                }
                SearchState(
                    autocompleteSuggestions = autocompleteSuggestions,
                    suggestionState = suggestionState,
                    query = query,
                )
            },
            onFailure = { throwable ->
                val errorState = ZarinaErrorScreenState.from(throwable)
                    .copy(buttonState = ZarinaErrorScreenButtonState(isButtonVisible = false))
                val suggestionState = SearchState.SuggestionState.Error(errorState)
                SearchState(
                    autocompleteSuggestions = persistentListOf(),
                    suggestionState = suggestionState,
                    query = query,
                )
            },
        )
    }

    private fun SearchSuggestions.toSearchSuggestionItems(): List<SearchSuggestionItem> {
        val suggestions = this
        return buildList {
            if (suggestions.querySuggestions.isNotEmpty()) {
                val titleText = Text.Resource(RCommon.string.res_search_results)
                add(SearchSuggestionItem.GenericTitle(titleText))

                val items = suggestions.querySuggestions
                    .take(SearchState.QUERY_SUGGESTION_MAX_COUNT)
                    .map { query ->
                        SearchSuggestionItem.QuerySuggestionItem(query.capitalize(Locale.current))
                    }
                addAll(items)
            }

            if (suggestions.categories.isNotEmpty()) {
                val titleText = Text.Resource(RCommon.string.res_categories)
                add(SearchSuggestionItem.GenericTitle(titleText))

                val items = suggestions.categories
                    .take(SearchState.CATEGORY_MAX_COUNT)
                    .map { it.toCategoryItem() }
                addAll(items)
            }
        }
    }

    private fun List<SearchHistoryQuery>.toSearchSuggestionItems(): List<SearchSuggestionItem> {
        val historyQueries = this
        return buildList {
            if (historyQueries.isNotEmpty()) {
                add(SearchSuggestionItem.SearchHistoryTitle)

                val items = historyQueries
                    .take(SearchState.SEARCH_HISTORY_QUERY_MAX_COUNT)
                    .map { query ->
                        SearchSuggestionItem.HistoryQueryItem(query.text.capitalize())
                    }
                addAll(items)
            }
        }
    }

    private fun SearchSuggestions.Category.toCategoryItem(): SearchSuggestionItem.CategoryItem {
        var name = this.name
        var parentCategoryChain = categoryParentCategoryChainRegex.find(name)?.value

        if (parentCategoryChain != null) {
            name = name.removeSuffix(parentCategoryChain).trim()
            parentCategoryChain = parentCategoryChain
                .removeSurrounding(BRACKET_START, BRACKET_END)
                .split(CATEGORY_PARENT_CATEGORY_CHAIN_SEPARATOR)
                .joinToString(separator = CATEGORY_PARENT_CATEGORY_CHAIN_SEPARATOR) {
                    it.capitalize(Locale.current)
                }
        }

        return SearchSuggestionItem.CategoryItem(
            id = this.id,
            name = name.capitalize(Locale.current),
            parentCategoryChain = parentCategoryChain,
        )
    }

    private companion object {
        private const val CATEGORY_PARENT_CATEGORY_CHAIN_PATTERN = "\\(.+\\)"
        private const val CATEGORY_PARENT_CATEGORY_CHAIN_SEPARATOR = " - "

        private const val BRACKET_START = "("
        private const val BRACKET_END = ")"
    }
}

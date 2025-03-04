package ru.livetyping.zarina.feature.search.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal data class SearchState(
    val autocompleteSuggestions: ImmutableList<SearchSuggestions.AutocompleteSuggestion>,
    val suggestionState: SuggestionState,
    val query: String,
) {
    @Stable
    internal sealed class SuggestionState {
        @Immutable
        data class Success(
            val items: ImmutableList<SearchSuggestionItem>,
        ) : SuggestionState()

        data object Empty : SuggestionState()

        @Immutable
        data class Error(val state: ZarinaErrorScreenState) : SuggestionState()
    }

    companion object {
        const val SEARCH_HISTORY_QUERY_MAX_COUNT = 5
        const val QUERY_SUGGESTION_MAX_COUNT = 5
        const val CATEGORY_MAX_COUNT = 5
    }
}

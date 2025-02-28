package ru.livetyping.zarina.feature.search.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions

internal sealed interface SearchEvent {
    data class SearchSuggestionItemClicked(val item: SearchSuggestionItem) : SearchEvent

    data class AutocompleteSuggestionClicked(
        val suggestion: SearchSuggestions.AutocompleteSuggestion,
    ) : SearchEvent

    data object ClearSearchHistoryClicked : SearchEvent

    data class DeleteSearchHistoryQueryItemClicked(
        val item: SearchSuggestionItem.HistoryQueryItem,
    ) : SearchEvent
}

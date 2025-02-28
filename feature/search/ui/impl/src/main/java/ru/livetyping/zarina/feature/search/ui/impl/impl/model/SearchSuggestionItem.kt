package ru.livetyping.zarina.feature.search.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.text.Text

@Stable
internal sealed class SearchSuggestionItem {
    @Immutable
    data class GenericTitle(val text: Text) : SearchSuggestionItem()

    data object SearchHistoryTitle : SearchSuggestionItem()

    @Immutable
    data class QuerySuggestionItem(val query: String) : SearchSuggestionItem()

    @Immutable
    data class HistoryQueryItem(val query: String) : SearchSuggestionItem()

    @Immutable
    data class CategoryItem(
        val id: Category.Id,
        val name: String,
        val parentCategoryChain: String?,
    ) : SearchSuggestionItem()
}
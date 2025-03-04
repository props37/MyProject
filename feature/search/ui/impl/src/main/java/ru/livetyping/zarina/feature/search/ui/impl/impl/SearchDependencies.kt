package ru.livetyping.zarina.feature.search.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.search.ClearSearchHistoryUseCase
import ru.livetyping.zarina.core.domain.usecase.search.DeleteSearchHistoryQueryUseCase
import ru.livetyping.zarina.core.domain.usecase.search.GetLastSearchHistoryQueriesFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.search.GetSearchSuggestionsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.search.SaveSearchHistoryQueryUseCase
import javax.inject.Inject

internal class SearchDependencies @Inject constructor(
    val getSearchSuggestionsFlow: GetSearchSuggestionsFlowUseCase,
    val getLastSearchHistoryQueriesFlow: GetLastSearchHistoryQueriesFlowUseCase,
    val saveSearchHistoryQuery: SaveSearchHistoryQueryUseCase,
    val deleteSearchHistoryQuery: DeleteSearchHistoryQueryUseCase,
    val clearSearchHistory: ClearSearchHistoryUseCase,
)

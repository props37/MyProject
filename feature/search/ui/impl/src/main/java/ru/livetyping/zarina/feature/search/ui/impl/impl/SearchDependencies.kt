package ru.livetyping.zarina.feature.search.ui.impl.impl

import ru.livetyping.zarina.core.domain.usecase.search.GetSearchSuggestionsFlowUseCase
import javax.inject.Inject

internal class SearchDependencies @Inject constructor(
    val getSearchSuggestionsFlow: GetSearchSuggestionsFlowUseCase,
)

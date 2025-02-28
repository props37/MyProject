package ru.livetyping.zarina.core.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.domain.usecase.search.GetSearchSuggestionsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetSearchSuggestionsFlowUseCaseImpl(
    private val searchRepository: SearchRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, SearchSuggestions>(logger), GetSearchSuggestionsFlowUseCase {

    override fun execute(params: Params): Flow<SearchSuggestions> {
        return searchRepository.getSearchSuggestionsFlow(params.query)
    }

    override fun invoke(params: Params): Flow<Result<SearchSuggestions>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetSearchSuggestionsFlowUseCaseImpl"
    }
}

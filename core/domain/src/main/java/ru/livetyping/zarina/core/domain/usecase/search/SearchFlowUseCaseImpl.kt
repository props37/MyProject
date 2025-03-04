package ru.livetyping.zarina.core.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchResult
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.domain.usecase.search.SearchFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SearchFlowUseCaseImpl(
    private val searchRepository: SearchRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, SearchResult>(logger), SearchFlowUseCase {

    override fun execute(params: Params): Flow<SearchResult> {
        return searchRepository.search(
            query = params.query,
            sorting = params.sorting,
            filters = params.filters,
            offset = params.offset,
        )
    }

    override fun invoke(params: Params): Flow<Result<SearchResult>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "SearchFlowUseCaseImpl"
    }
}

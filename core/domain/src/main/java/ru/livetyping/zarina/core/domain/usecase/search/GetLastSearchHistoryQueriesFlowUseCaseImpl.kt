package ru.livetyping.zarina.core.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.domain.usecase.search.GetLastSearchHistoryQueriesFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetLastSearchHistoryQueriesFlowUseCaseImpl(
    private val searchRepository: SearchRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<SearchHistoryQuery>>(logger),
    GetLastSearchHistoryQueriesFlowUseCase {

    override fun execute(params: Params): Flow<List<SearchHistoryQuery>> {
        return searchRepository.getLastSearchHistoryQueriesFlow(
            query = params.query.trim().lowercase(),
            limit = params.limit,
        )
    }

    override fun invoke(params: Params): Flow<Result<List<SearchHistoryQuery>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetLastSearchHistoryQueriesFlowUseCaseImpl"
    }
}

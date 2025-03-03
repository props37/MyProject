package ru.livetyping.zarina.core.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetLastSearchHistoryQueriesFlowFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<SearchHistoryQuery>>>

    public data class Params(
        val query: String,
        val limit: Int,
    )

    public companion object {
        public fun getInstance(
            searchRepository: SearchRepository,
            logger: UseCaseLogger?,
        ): GetLastSearchHistoryQueriesFlowFlowUseCase {
            return GetLastSearchHistoryQueriesFlowFlowUseCaseImpl(
                searchRepository = searchRepository,
                logger = logger,
            )
        }
    }
}

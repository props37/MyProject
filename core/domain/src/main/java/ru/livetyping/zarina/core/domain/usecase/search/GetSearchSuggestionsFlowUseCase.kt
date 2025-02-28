package ru.livetyping.zarina.core.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.search.SearchSuggestions
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetSearchSuggestionsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<SearchSuggestions>>

    public data class Params(val query: String)

    public companion object {
        public fun getInstance(
            searchRepository: SearchRepository,
            logger: UseCaseLogger?,
        ): GetSearchSuggestionsFlowUseCase {
            return GetSearchSuggestionsFlowUseCaseImpl(
                searchRepository = searchRepository,
                logger = logger,
            )
        }
    }
}

package ru.livetyping.zarina.core.domain.usecase.search

import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SaveSearchHistoryQueryUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val query: String)

    public companion object {
        public fun getInstance(
            searchRepository: SearchRepository,
            logger: UseCaseLogger?,
        ): SaveSearchHistoryQueryUseCase {
            return SaveSearchHistoryQueryUseCaseImpl(
                searchRepository = searchRepository,
                logger = logger,
            )
        }
    }
}

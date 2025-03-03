package ru.livetyping.zarina.core.domain.usecase.search

import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface DeleteSearchHistoryQueryUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(val query: String)

    public companion object {
        public fun getInstance(
            searchRepository: SearchRepository,
            logger: UseCaseLogger?,
        ): DeleteSearchHistoryQueryUseCase {
            return DeleteSearchHistoryQueryUseCaseImpl(
                searchRepository = searchRepository,
                logger = logger,
            )
        }
    }
}

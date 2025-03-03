package ru.livetyping.zarina.core.domain.usecase.search

import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ClearSearchHistoryUseCase {
    public suspend operator fun invoke(): Result<Unit>

    public companion object {
        public fun getInstance(
            searchRepository: SearchRepository,
            logger: UseCaseLogger?,
        ): ClearSearchHistoryUseCase {
            return ClearSearchHistoryUseCaseImpl(
                searchRepository = searchRepository,
                logger = logger,
            )
        }
    }
}

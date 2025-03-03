package ru.livetyping.zarina.core.domain.usecase.search

import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ClearSearchHistoryUseCaseImpl(
    private val searchRepository: SearchRepository,
    logger: UseCaseLogger?,
) : UseCase<Unit, Unit>(logger), ClearSearchHistoryUseCase {

    override suspend fun execute(params: Unit) {
        searchRepository.clearSearchHistory()
    }

    override suspend fun invoke(): Result<Unit> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "ClearSearchHistoryUseCaseImpl"
    }
}

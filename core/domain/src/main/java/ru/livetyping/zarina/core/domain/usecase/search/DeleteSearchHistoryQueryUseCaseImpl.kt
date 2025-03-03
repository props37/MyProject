package ru.livetyping.zarina.core.domain.usecase.search

import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.domain.usecase.search.DeleteSearchHistoryQueryUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class DeleteSearchHistoryQueryUseCaseImpl(
    private val searchRepository: SearchRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), DeleteSearchHistoryQueryUseCase {

    override suspend fun execute(params: Params) {
        val query = params.query.trim().lowercase()
        searchRepository.deleteSearchHistoryQuery(query)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "DeleteSearchHistoryQueryUseCaseImpl"
    }
}

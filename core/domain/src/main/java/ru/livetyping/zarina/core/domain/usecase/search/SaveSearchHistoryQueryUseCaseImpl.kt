package ru.livetyping.zarina.core.domain.usecase.search

import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.domain.usecase.search.SaveSearchHistoryQueryUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class SaveSearchHistoryQueryUseCaseImpl(
    private val searchRepository: SearchRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), SaveSearchHistoryQueryUseCase {

    override suspend fun execute(params: Params) {
        val query = SearchHistoryQuery(
            text = params.query.trim().lowercase(),
            timestampMillis = System.currentTimeMillis(),
        )
        searchRepository.saveSearchHistoryQuery(query)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "SaveSearchHistoryQueryUseCaseImpl"
    }
}

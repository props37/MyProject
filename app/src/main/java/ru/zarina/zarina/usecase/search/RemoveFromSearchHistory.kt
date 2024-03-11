package ru.zarina.zarina.usecase.search

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.search.ISearchRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.base.usecase.UseCase

@Factory
class RemoveFromSearchHistoryUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val searchRepository: ISearchRepository,
) : UseCase<RemoveFromSearchHistoryUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val (query) = params

        return searchRepository.removeFromHistory(query)
    }

    data class Params(
        val query: String,
    )

}

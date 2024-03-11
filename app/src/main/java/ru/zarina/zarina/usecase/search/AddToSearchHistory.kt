package ru.zarina.zarina.usecase.search

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.search.ISearchRepository
import ru.zarina.zarina.di.Qualifiers

@Factory
class AddToSearchHistoryUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val searchRepository: ISearchRepository,
) : UseCase<AddToSearchHistoryUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val (query) = params

        return searchRepository.addToHistory(query)
    }

    data class Params(
        val query: String,
    )

}

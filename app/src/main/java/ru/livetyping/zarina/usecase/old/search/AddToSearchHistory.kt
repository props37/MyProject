package ru.livetyping.zarina.usecase.old.search

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.search.ISearchRepository
import ru.livetyping.zarina.di.old.Qualifiers

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

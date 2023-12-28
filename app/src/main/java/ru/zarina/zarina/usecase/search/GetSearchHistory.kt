package ru.zarina.zarina.usecase.search

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.search.ISearchRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.usecase.base.FlowUseCase

@Factory
class GetSearchHistoryUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val searchRepository: ISearchRepository,
) : FlowUseCase<GetSearchHistoryUseCase.Params, List<String>>(dispatcher) {

    override fun execute(params: Params): Flow<List<String>> {
        val (limit) = params

        return searchRepository.getHistory(limit)
    }

    data class Params(
        val limit: Int = 15,
    )
}

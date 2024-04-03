package ru.livetyping.zarina.usecase.old.search

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.old.search.ISearchRepository
import ru.livetyping.zarina.di.old.Qualifiers

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

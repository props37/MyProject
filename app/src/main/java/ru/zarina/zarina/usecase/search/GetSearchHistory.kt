package ru.zarina.zarina.usecase.search

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.data.search.ISearchRepository
import ru.zarina.zarina.di.Qualifiers

@Factory
class GetSearchHistoryUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val searchRepository: ISearchRepository,
) : FlowUseCase<Unit, List<String>>(dispatcher) {

    override fun execute(params: Unit): Flow<Result<List<String>>> {
        return searchRepository.getHistory().map { Result.success(it) }
    }

}

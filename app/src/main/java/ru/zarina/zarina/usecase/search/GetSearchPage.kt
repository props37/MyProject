package ru.zarina.zarina.usecase.search

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.search.ISearchRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.Product

@Factory
class GetSearchPageUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val searchRepository: ISearchRepository,
) : UseCase<GetSearchPageUseCase.Parameters, Page<List<Product>>>(dispatcher) {

    override suspend fun execute(params: Parameters): Page<List<Product>> {
        val (query, pageIndex) = params

        searchRepository.addToHistory(query)

        return searchRepository.getSearchPage(query, pageIndex)
    }

    data class Parameters(
        val query: String,
        val pageIndex: Int,
    )

}

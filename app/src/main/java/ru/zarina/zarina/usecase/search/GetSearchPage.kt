package ru.zarina.zarina.usecase.search

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.search.ISearchRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.FilteredProducts
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Page
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.base.usecase.UseCase

@Factory
class GetSearchPageUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val searchRepository: ISearchRepository,
) : UseCase<GetSearchPageUseCase.Params, Page<FilteredProducts>>(dispatcher) {

    override suspend fun execute(params: Params): Page<FilteredProducts> {
        val (query, sort, filtration, pageIndex) = params

        return searchRepository.getSearchPage(query, sort, filtration, pageIndex)
    }

    data class Params(
        val query: String,
        val sort: ProductSort,
        val filtration: Filtration?,
        val pageIndex: Int,
    )

    companion object {
        const val PAGE_SIZE = 10
    }

}

package ru.livetyping.zarina.usecase.old.search

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.search.ISearchRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.FilteredProducts
import ru.livetyping.zarina.domain.old.Filtration
import ru.livetyping.zarina.domain.old.Page
import ru.livetyping.zarina.domain.old.ProductSort

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

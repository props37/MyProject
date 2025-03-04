package ru.livetyping.zarina.core.domain.usecase.search

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductSorting
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.domain.model.search.SearchResult
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SearchFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<SearchResult>>

    public data class Params(
        val query: String,
        val sorting: ProductSorting,
        val filters: ProductFilters?,
        val offset: Int,
    )

    public companion object {
        public fun getInstance(
            searchRepository: SearchRepository,
            logger: UseCaseLogger?,
        ): SearchFlowUseCase {
            return SearchFlowUseCaseImpl(
                searchRepository = searchRepository,
                logger = logger,
            )
        }
    }
}

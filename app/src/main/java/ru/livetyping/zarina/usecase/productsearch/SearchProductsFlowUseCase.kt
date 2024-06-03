package ru.livetyping.zarina.usecase.productsearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Sorting
import ru.livetyping.zarina.domain.productsearch.ProductSearchResult
import javax.inject.Inject

// TODO: [High] Add filters parameter

class SearchProductsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productSearchRepository: ProductSearchRepository,
) : FlowUseCase<SearchProductsFlowUseCase.Params, ProductSearchResult>(dispatcher) {

    override fun execute(params: Params): Flow<ProductSearchResult> {
        return productSearchRepository.searchProductsFlow(
            params.query,
            params.sorting,
            params.offset,
        )
    }

    data class Params(
        val query: String,
        val sorting: Sorting,
        val offset: Int,
    )
}

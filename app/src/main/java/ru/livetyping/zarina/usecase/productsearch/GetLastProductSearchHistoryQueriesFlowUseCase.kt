package ru.livetyping.zarina.usecase.productsearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryQuery
import javax.inject.Inject

class GetLastProductSearchHistoryQueriesFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productSearchRepository: ProductSearchRepository,
) : FlowUseCase<GetLastProductSearchHistoryQueriesFlowUseCase.Params, List<ProductSearchHistoryQuery>>(
    dispatcher,
) {

    override fun execute(params: Params): Flow<List<ProductSearchHistoryQuery>> {
        return productSearchRepository.getLastProductSearchHistoryQueriesFlow(
            text = params.text,
            limit = params.limit,
        )
    }

    data class Params(val text: String, val limit: Int)
}

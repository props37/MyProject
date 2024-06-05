package ru.livetyping.zarina.usecase.productsearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryEntry
import javax.inject.Inject

class GetLastProductSearchHistoryEntriesFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productSearchRepository: ProductSearchRepository,
) : FlowUseCase<GetLastProductSearchHistoryEntriesFlowUseCase.Params, List<ProductSearchHistoryEntry>>(
    dispatcher,
) {

    override fun execute(params: Params): Flow<List<ProductSearchHistoryEntry>> {
        return productSearchRepository.getLastProductSearchHistoryEntriesFlow(
            text = params.text,
            limit = params.limit,
        )
    }

    data class Params(val text: String, val limit: Int)
}

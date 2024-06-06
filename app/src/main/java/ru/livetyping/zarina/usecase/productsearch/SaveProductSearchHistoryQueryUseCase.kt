package ru.livetyping.zarina.usecase.productsearch

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryQuery
import timber.log.Timber
import javax.inject.Inject

class SaveProductSearchHistoryQueryUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productSearchRepository: ProductSearchRepository,
) : UseCase<SaveProductSearchHistoryQueryUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val query = params.query
        Timber.v("Save product search history query: $query")
        productSearchRepository.saveProductSearchHistoryQuery(query)
    }

    data class Params(val query: ProductSearchHistoryQuery)
}

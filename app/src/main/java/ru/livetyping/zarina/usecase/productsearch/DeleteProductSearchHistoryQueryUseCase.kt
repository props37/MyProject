package ru.livetyping.zarina.usecase.productsearch

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class DeleteProductSearchHistoryQueryUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productSearchRepository: ProductSearchRepository,
) : UseCase<DeleteProductSearchHistoryQueryUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val text = params.text
        Timber.v("Delete product search history query: $text")
        productSearchRepository.deleteProductSearchHistoryQuery(text)
    }

    data class Params(val text: String)
}

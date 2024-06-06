package ru.livetyping.zarina.usecase.productsearch

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class ClearProductSearchHistoryUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productSearchRepository: ProductSearchRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Clear product search history")
        productSearchRepository.clearProductSearchHistory()
    }
}

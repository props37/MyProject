package ru.livetyping.zarina.usecase.productsearch

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.productsearch.ProductSearchRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryEntry
import timber.log.Timber
import javax.inject.Inject

class SaveProductSearchHistoryEntryUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productSearchRepository: ProductSearchRepository,
) : UseCase<SaveProductSearchHistoryEntryUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val entry = params.entry
        Timber.v("Save product search history entry: $entry")
        productSearchRepository.saveProductSearchHistoryEntry(entry)
    }

    data class Params(val entry: ProductSearchHistoryEntry)
}

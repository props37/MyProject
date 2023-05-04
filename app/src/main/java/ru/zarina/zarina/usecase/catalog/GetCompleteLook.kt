package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.Product
import timber.log.Timber
import javax.inject.Inject

class GetCompleteLookUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
) : UseCase<GetCompleteLookUseCase.Params, List<Product>>(dispatcher) {
    override suspend fun execute(params: Params): List<Product> {
        val (product) = params

        val completeLook = productRepository.getCompleteLook(product)
        Timber.v("Complete look for $product contains ${completeLook.size} items")

        return completeLook
    }

    data class Params(
        val product: Product,
    )
}

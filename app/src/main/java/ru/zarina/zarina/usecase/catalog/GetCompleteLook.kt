package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Product
import timber.log.Timber

@Factory
class GetCompleteLookUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
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

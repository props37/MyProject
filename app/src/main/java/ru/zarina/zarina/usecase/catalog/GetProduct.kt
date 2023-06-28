package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Product

@Factory
class GetProductUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
) : UseCase<GetProductUseCase.Params, Product>(dispatcher) {

    override suspend fun execute(params: Params): Product {
        val (id) = params

        return productRepository.getProduct(id)
    }

    data class Params(
        val id: Product.Id,
    )

}

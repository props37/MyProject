package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.Product
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
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

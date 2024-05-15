package ru.livetyping.zarina.usecase.product

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.product.ProductRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import javax.inject.Inject

class GetProductSimilarFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val productRepository: ProductRepository,
) : FlowUseCase<GetProductSimilarFlowUseCase.Params, List<ProductItem>>(dispatcher) {

    override fun execute(params: Params): Flow<List<ProductItem>> {
        return productRepository.getProductSimilarFlow(params.productId)
    }

    data class Params(val productId: Product.Id)
}

package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetSimilarProductsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetSimilarProductsFlowUseCaseImpl(
    private val productRepository: ProductRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<ProductShort>>(logger), GetSimilarProductsFlowUseCase {

    override fun execute(params: Params): Flow<List<ProductShort>> {
        return productRepository.getSimilarProductsFlow(params.productId)
    }

    override fun invoke(params: Params): Flow<Result<List<ProductShort>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetSimilarProductsFlowUseCaseImpl"
    }
}

package ru.livetyping.zarina.core.domain.usecase.product

import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetSimilarProductsUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetSimilarProductsUseCaseImpl(
    private val productRepository: ProductRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, List<ProductShort>>(logger), GetSimilarProductsUseCase {

    override suspend fun execute(params: Params): List<ProductShort> {
        return productRepository.getSimilarProducts(params.productId)
    }

    override suspend fun invoke(params: Params): Result<List<ProductShort>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetSimilarProductsFlowUseCaseImpl"
    }
}

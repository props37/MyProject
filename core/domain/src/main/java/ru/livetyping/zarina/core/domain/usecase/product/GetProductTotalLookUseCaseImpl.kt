package ru.livetyping.zarina.core.domain.usecase.product

import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetProductTotalLookUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetProductTotalLookUseCaseImpl(
    private val productRepository: ProductRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, List<ProductShort>>(logger), GetProductTotalLookUseCase {

    override suspend fun execute(params: Params): List<ProductShort> {
        return productRepository.getProductTotalLook(params.productId)
    }

    override suspend fun invoke(params: Params): Result<List<ProductShort>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetProductTotalLookFlowUseCaseImpl"
    }
}

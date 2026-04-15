// ========== REVIEW FROM HERE ==========
package ru.livetyping.zarina.core.domain.usecase.product

import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetProductAiReviewsUseCaseImpl(
    private val productRepository: ProductRepository,
    logger: UseCaseLogger?,
): UseCase<GetProductAiReviewsUseCase.Params, ProductAiReviews>(logger), GetProductAiReviewsUseCase {
    override suspend fun execute(params: GetProductAiReviewsUseCase.Params): ProductAiReviews {
        return productRepository.getProductAiReviews(params.groupId)
    }

    override suspend fun invoke(params: GetProductAiReviewsUseCase.Params): Result<ProductAiReviews> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetProductAiReviewsUseCaseImpl"
    }
}
// ========== TO HERE ==========
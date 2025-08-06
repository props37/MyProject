package ru.livetyping.zarina.core.domain.usecase.product

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetProductAiReviewsUseCase {
    public suspend operator fun invoke(params: Params): Result<ProductAiReviews>

    public data class Params(val productId: Product.Id)

    public companion object {
        public fun getInstance(
            productRepository: ProductRepository,
            logger: UseCaseLogger?
        ): GetProductAiReviewsUseCase {
            return GetProductAiReviewsUseCaseImpl(productRepository, logger)
        }
    }
}
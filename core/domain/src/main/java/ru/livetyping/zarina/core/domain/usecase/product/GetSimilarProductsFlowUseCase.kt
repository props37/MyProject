package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetSimilarProductsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<ProductShort>>>

    public data class Params(val productId: Product.Id)

    public companion object {
        public fun getInstance(
            productRepository: ProductRepository,
            logger: UseCaseLogger?,
        ): GetSimilarProductsFlowUseCase {
            return GetSimilarProductsFlowUseCaseImpl(
                productRepository = productRepository,
                logger = logger,
            )
        }
    }
}

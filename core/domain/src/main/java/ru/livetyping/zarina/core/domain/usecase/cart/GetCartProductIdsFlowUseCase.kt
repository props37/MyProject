package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCartProductIdsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<Set<Product.Id>>>

    public data class Params(val cachePolicy: CachePolicy)

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): GetCartProductIdsFlowUseCase {
            return GetCartProductIdsFlowUseCaseImpl(
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}

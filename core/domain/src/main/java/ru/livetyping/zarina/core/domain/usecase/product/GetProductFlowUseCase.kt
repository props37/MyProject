package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetProductFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<ProductDetailed>>

    public data class Params(val productId: Product.Id)

    public companion object {
        public fun getInstance(
            productRepository: ProductRepository,
            cartRepository: CartRepository,
            wishlistRepository: WishlistRepository,
            logger: UseCaseLogger?,
        ): GetProductFlowUseCase {
            return GetProductFlowUseCaseImpl(
                productRepository = productRepository,
                cartRepository = cartRepository,
                wishlistRepository = wishlistRepository,
                logger = logger,
            )
        }
    }
}

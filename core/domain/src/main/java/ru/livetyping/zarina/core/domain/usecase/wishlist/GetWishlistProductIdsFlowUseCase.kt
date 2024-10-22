package ru.livetyping.zarina.core.domain.usecase.wishlist

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetWishlistProductIdsFlowUseCase {
    public operator fun invoke(): Flow<Result<Set<Product.Id>>>

    public companion object {
        public fun getInstance(
            wishlistRepository: WishlistRepository,
            logger: UseCaseLogger?,
        ): GetWishlistProductIdsFlowUseCase {
            return GetWishlistProductIdsFlowUseCaseImpl(
                wishlistRepository = wishlistRepository,
                logger = logger,
            )
        }
    }
}

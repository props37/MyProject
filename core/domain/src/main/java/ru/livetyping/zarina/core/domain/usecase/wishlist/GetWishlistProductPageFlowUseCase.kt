package ru.livetyping.zarina.core.domain.usecase.wishlist

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetWishlistProductPageFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<Page<List<ProductShort>>>>

    public data class Params(val page: Int)

    public companion object {
        public fun getInstance(
            wishlistRepository: WishlistRepository,
            logger: UseCaseLogger?,
        ): GetWishlistProductPageFlowUseCase {
            return GetWishlistProductPageFlowUseCaseImpl(
                wishlistRepository = wishlistRepository,
                logger = logger,
            )
        }
    }
}

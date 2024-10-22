package ru.livetyping.zarina.core.domain.usecase.wishlist

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ToggleProductInWishlistUseCase {
    public suspend operator fun invoke(params: Params): Result<Boolean>

    public data class Params(val productId: Product.Id)

    public companion object {
        public fun getInstance(
            wishlistRepository: WishlistRepository,
            logger: UseCaseLogger?,
        ): ToggleProductInWishlistUseCase {
            return ToggleProductInWishlistUseCaseImpl(
                wishlistRepository = wishlistRepository,
                logger = logger,
            )
        }
    }
}

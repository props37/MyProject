package ru.livetyping.zarina.core.domain.usecase.wishlist

import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ClearWishlistUseCase {
    public suspend operator fun invoke(): Result<Unit>

    public companion object {
        public fun getInstance(
            wishlistRepository: WishlistRepository,
            logger: UseCaseLogger?,
        ): ClearWishlistUseCase {
            return ClearWishlistUseCaseImpl(
                wishlistRepository = wishlistRepository,
                logger = logger,
            )
        }
    }
}

package ru.livetyping.zarina.core.domain.usecase.wishlist

import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ClearWishlistUseCaseImpl(
    private val wishlistRepository: WishlistRepository,
    logger: UseCaseLogger?,
) : UseCase<Unit, Unit>(logger), ClearWishlistUseCase {

    override suspend fun execute(params: Unit) {
        wishlistRepository.clearWishlist()
    }

    override suspend fun invoke(): Result<Unit> {
        return call(Unit)
    }
}

package ru.livetyping.zarina.core.domain.usecase.wishlist

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ToggleProductInWishlistUseCaseImpl(
    private val wishlistRepository: WishlistRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Boolean>(logger), ToggleProductInWishlistUseCase {

    override suspend fun execute(params: Params): Boolean {
        val productId = params.productId
        val wishlistProductIds =
            wishlistRepository.getWishlistProductIdsFlow().firstOrNull() ?: emptySet()
        val isProductInWishlist = if (productId in wishlistProductIds) {
            wishlistRepository.removeProductFromWishlist(productId)
            false
        } else {
            wishlistRepository.addProductToWishlist(productId)
            true
        }
        return isProductInWishlist
    }

    override suspend fun invoke(params: Params): Result<Boolean> {
        return call(params)
    }
}

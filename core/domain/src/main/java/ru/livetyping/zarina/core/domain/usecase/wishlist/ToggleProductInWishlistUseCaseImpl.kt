package ru.livetyping.zarina.core.domain.usecase.wishlist

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.wishlist.ToggleProductInWishlistUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ToggleProductInWishlistUseCaseImpl(
    private val wishlistRepository: WishlistRepository,
    private val logger: UseCaseLogger?,
) : UseCase<Params, Boolean>(logger), ToggleProductInWishlistUseCase {

    override suspend fun execute(params: Params): Boolean {
        val productId = params.productId

        val wishlistProductIds =
            wishlistRepository.getWishlistProductIdsFlow(CachePolicy.LocalOnly).firstOrNull()
                ?: emptySet()
        val isProductInWishlist = if (productId in wishlistProductIds) {
            wishlistRepository.removeProductFromWishlist(productId)
            false
        } else {
            wishlistRepository.addProductToWishlist(productId)
            true
        }

        if (!wishlistRepository.isWishlistProductIdsFetched()) {
            fetchWishlistProductIds()
        }

        return isProductInWishlist
    }

    override suspend fun invoke(params: Params): Result<Boolean> {
        return call(params)
    }

    private suspend fun fetchWishlistProductIds() {
        try {
            val cachePolicy = CachePolicy.Remote()
            wishlistRepository.getWishlistProductIdsFlow(cachePolicy).firstOrNull()
        } catch (e: Exception) {
            logger?.e(TAG, e, "Failed to fetch wishlist product IDs")
        }
    }

    private companion object {
        private const val TAG = "ToggleProductInWishlistUseCaseImpl"
    }
}

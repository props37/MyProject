package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetProductTotalLookUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetProductTotalLookUseCaseImpl(
    private val productRepository: ProductRepository,
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, List<ProductShort>>(logger), GetProductTotalLookUseCase {

    override suspend fun execute(params: Params): List<ProductShort> {
        val products = productRepository.getProductTotalLook(params.productId)
        val wishlistProductIds =
            wishlistRepository.getWishlistProductIdsFlow(CachePolicy.LocalFirstThenRemote())
                .firstOrNull() ?: emptySet()
        val cartProductIds =
            cartRepository.getCartProductIdsFlow(CachePolicy.LocalFirstThenRemote())
                .firstOrNull() ?: emptySet()

        return products.map { product ->
            product.copy(
                isInWishlist = product.id in wishlistProductIds,
                isInCart = product.id in cartProductIds,
            )
        }
    }

    override suspend fun invoke(params: Params): Result<List<ProductShort>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetProductTotalLookFlowUseCaseImpl"
    }
}

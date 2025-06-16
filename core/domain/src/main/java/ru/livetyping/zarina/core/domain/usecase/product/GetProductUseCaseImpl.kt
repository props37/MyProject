package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetProductUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetProductUseCaseImpl(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, ProductDetailed>(logger), GetProductUseCase {

    override suspend fun execute(params: Params): ProductDetailed {
        val product = productRepository.getProduct(params.productId)
        val cartProductIds =
            cartRepository.getCartProductIdsFlow(CachePolicy.LocalFirstThenRemote())
                .firstOrNull() ?: emptySet()
        val wishlistProductIds =
            wishlistRepository.getWishlistProductIdsFlow(CachePolicy.LocalFirstThenRemote())
                .firstOrNull() ?: emptySet()
        return product.copy(
            isInWishlist = product.id in wishlistProductIds,
            isInCart = product.id in cartProductIds,
        )
    }

    override suspend fun invoke(params: Params): Result<ProductDetailed> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetProductFlowUseCaseImpl"
    }
}

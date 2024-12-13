package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetProductFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetProductFlowUseCaseImpl(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, ProductDetailed>(logger), GetProductFlowUseCase {

    override fun execute(params: Params): Flow<ProductDetailed> {
        val productFlow = productRepository.getProductFlow(params.productId)
        return combine(
            productFlow,
            cartRepository.getCartProductIdsFlow(CachePolicy.LocalFirstThenRemote()),
            wishlistRepository.getWishlistProductIdsFlow(CachePolicy.LocalFirstThenRemote()),
        ) { product, cartProductIds, wishlistProductIds ->
            product.copy(
                isInWishlist = product.id in wishlistProductIds,
                isInCart = product.id in cartProductIds,
            )
        }
    }

    override fun invoke(params: Params): Flow<Result<ProductDetailed>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetProductFlowUseCaseImpl"
    }
}

package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCartFlowUseCaseImpl(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val wishlistRepository: WishlistRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Cart>(logger), GetCartFlowUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<Cart> {
        val wishlistProductIdsFlow =
            wishlistRepository.getWishlistProductIdsFlow(CachePolicy.LocalOnly)
        return userRepository.getUserCityFlow(CachePolicy.LocalOnly)
            .flatMapLatest { city ->
                cartRepository.getCartFlow(params.cartType, city?.id)
            }
            .combine(wishlistProductIdsFlow) { cart, wishlistProductIds ->
                val products = cart.products.map { product ->
                    product.copy(isInWishlist = product.productId in wishlistProductIds)
                }
                cart.copy(products = products)
            }
    }

    override fun invoke(params: Params): Flow<Result<Cart>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCartFlowUseCaseImpl"
    }
}

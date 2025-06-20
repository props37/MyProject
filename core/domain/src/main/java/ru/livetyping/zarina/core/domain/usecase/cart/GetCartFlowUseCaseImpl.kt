package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.product.Product
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
        return if (params.cityFiasId != null) {
            getCartFlow(params.cartType, params.cityFiasId, wishlistProductIdsFlow)
        } else {
            userRepository.getUserCityFlow(CachePolicy.LocalOnly)
                .flatMapLatest { city ->
                    getCartFlow(params.cartType, city?.id, wishlistProductIdsFlow)
                    cartRepository.getCartFlow(params.cartType, city?.id)
                }
        }
    }

    override fun invoke(params: Params): Flow<Result<Cart>> {
        return call(params)
    }

    private fun getCartFlow(
        cartType: CartType,
        cityFiasId: FiasId?,
        wishlistProductIdsFlow: Flow<Set<Product.Id>>,
    ): Flow<Cart> {
        return cartRepository.getCartFlow(cartType, cityFiasId)
            .combine(wishlistProductIdsFlow) { cart, wishlistProductIds ->
                val products = cart.products.map { product ->
                    product.copy(isInWishlist = product.productId in wishlistProductIds)
                }
                cart.copy(products = products)
            }
    }

    private companion object {
        private const val TAG = "GetCartFlowUseCaseImpl"
    }
}

package ru.livetyping.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.cart.CartRepository
import ru.livetyping.zarina.data.favorite.FavoriteRepository
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.cart.DeliveryType
import javax.inject.Inject

class GetCartFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository,
) : FlowUseCase<GetCartFlowUseCase.Params, Cart>(dispatcher) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<Cart> {
        val deliveryType = params.deliveryType
        return userRepository.getUserCityFlow()
            .flatMapLatest { city ->
                cartRepository.getCartFlow(deliveryType, city?.kladrId)
            }
            .combine(favoriteRepository.favoriteProductIds) { cart, favoriteProductIds ->
                val products = cart.products.map { product ->
                    product.copy(isInFavorites = product.productId in favoriteProductIds)
                }
                cart.copy(products = products)
            }
    }

    data class Params(val deliveryType: DeliveryType)
}

package ru.zarina.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.cart.CartRepository
import ru.zarina.zarina.data.user.UserRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.cart.Cart
import ru.zarina.zarina.domain.cart.DeliveryType
import javax.inject.Inject

class GetCartFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
) : FlowUseCase<GetCartFlowUseCase.Params, Cart>(dispatcher) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<Cart> {
        val deliveryType = params.deliveryType
        return userRepository.getUserCityFlow().flatMapLatest { city ->
            cartRepository.getCartFlow(deliveryType, city?.kladrId)
                .onEach { cart ->
                    cartRepository.setCartSize(cart.size)
                }
        }
    }

    data class Params(val deliveryType: DeliveryType)
}

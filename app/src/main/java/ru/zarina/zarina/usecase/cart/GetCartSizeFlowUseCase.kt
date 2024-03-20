package ru.zarina.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.cart.CartRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.cart.CartSize
import javax.inject.Inject

class GetCartSizeFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : FlowUseCase<Unit, CartSize>(dispatcher) {

    override fun execute(params: Unit): Flow<CartSize> {
        return cartRepository.cartSize
    }
}

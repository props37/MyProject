package ru.zarina.zarina.usecase.rework.cart

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.cart.CartRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.base.usecase.FlowUseCase
import javax.inject.Inject

class GetCartProductCountFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : FlowUseCase<Unit, Int>(dispatcher) {

    override fun execute(params: Unit): Flow<Int> {
        return cartRepository.cartProductCount
    }
}

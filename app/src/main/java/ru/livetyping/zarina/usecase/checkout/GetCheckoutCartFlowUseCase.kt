package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.Cart
import ru.livetyping.zarina.domain.checkout.CheckoutParams
import javax.inject.Inject

class GetCheckoutCartFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val checkoutRepository: CheckoutRepository,
) : FlowUseCase<GetCheckoutCartFlowUseCase.Params, Cart>(dispatcher) {

    override fun execute(params: Params): Flow<Cart> {
        return checkoutRepository.getCartFlow(params.checkoutParams)
    }

    data class Params(val checkoutParams: CheckoutParams)
}

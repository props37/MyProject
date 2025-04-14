package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.GetCheckoutCartFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCheckoutCartFlowUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, Cart>(logger), GetCheckoutCartFlowUseCase {

    override fun execute(params: Params): Flow<Cart> {
        return checkoutRepository.getCartFlow(params.checkoutParams, params.paymentMethod)
    }

    override fun invoke(params: Params): Flow<Result<Cart>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCheckoutCartFlowUseCaseImpl"
    }
}

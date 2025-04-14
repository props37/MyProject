package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPaymentMethodsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetPaymentMethodsFlowUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<PaymentMethod>>(logger), GetPaymentMethodsFlowUseCase {

    override fun execute(params: Params): Flow<List<PaymentMethod>> {
        return checkoutRepository.getPaymentMethodsFlow(params.checkoutParams, params.cart)
    }

    override fun invoke(params: Params): Flow<Result<List<PaymentMethod>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetPaymentMethodsFlowUseCaseImpl"
    }
}

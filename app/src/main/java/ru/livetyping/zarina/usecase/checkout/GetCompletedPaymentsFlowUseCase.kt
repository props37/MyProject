package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.domain.checkout.PaymentData
import javax.inject.Inject

class GetCompletedPaymentsFlowUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
) : FlowUseCase<Unit, PaymentData>() {

    override fun execute(params: Unit): Flow<PaymentData> {
        return checkoutRepository.getCompletedPaymentsFlow()
    }
}

package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.checkout.PaymentData
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCompletedPaymentsFlowUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, PaymentData>(logger), GetCompletedPaymentsFlowUseCase {

    override fun execute(params: Unit): Flow<PaymentData> {
        return checkoutRepository.getCompletedPaymentsFlow()
    }

    override fun invoke(): Flow<Result<PaymentData>> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "GetCompletedPaymentsFlowUseCaseImpl"
    }
}

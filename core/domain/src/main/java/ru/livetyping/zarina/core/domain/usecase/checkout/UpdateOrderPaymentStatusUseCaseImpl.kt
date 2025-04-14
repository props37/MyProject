package ru.livetyping.zarina.core.domain.usecase.checkout

import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.UpdateOrderPaymentStatusUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class UpdateOrderPaymentStatusUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), UpdateOrderPaymentStatusUseCase {

    override suspend fun execute(params: Params) {
        checkoutRepository.updateOrderPaymentStatus(params.orderId, params.paymentMethodType)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "UpdateOrderPaymentStatusUseCaseImpl"
    }
}

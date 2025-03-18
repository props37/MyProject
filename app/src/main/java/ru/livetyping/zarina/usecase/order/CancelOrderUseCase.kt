package ru.livetyping.zarina.usecase.order

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.order.OrderRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.order.Order
import timber.log.Timber
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val orderRepository: OrderRepository,
) : UseCase<CancelOrderUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val orderId = params.orderId
        Timber.v("Cancel order $orderId")
        orderRepository.cancelOrder(orderId)
        AppMetricaHelper.reportOrderCancelled(orderId)
    }

    data class Params(val orderId: Order.Id)
}

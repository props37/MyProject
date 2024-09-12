package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.checkout.PickupPoint
import ru.livetyping.zarina.domain.checkout.PickupPointDetails
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class GetPickupPointDetailsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val checkoutRepository: CheckoutRepository,
) : FlowUseCase<GetPickupPointDetailsFlowUseCase.Params, PickupPointDetails>(dispatcher) {

    override fun execute(params: Params): Flow<PickupPointDetails> {
        return checkoutRepository.getPickupPointDetailsFlow(
            cityKladrId = params.cityKladrId,
            pickupPointId = params.pickupPointId,
        )
    }

    data class Params(
        val cityKladrId: KladrId,
        val pickupPointId: PickupPoint.Id,
    )
}

package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.checkout.DeliveryOptions
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class GetPostDeliveryOptionsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val checkoutRepository: CheckoutRepository,
) : FlowUseCase<GetPostDeliveryOptionsFlowUseCase.Params, DeliveryOptions>(dispatcher) {

    override fun execute(params: Params): Flow<DeliveryOptions> {
        return checkoutRepository.getPostDeliveryOptionsFlow(params.buildingKladrId)
    }

    data class Params(
        val buildingKladrId: KladrId,
    )
}

package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.DeliveryMethod
import ru.livetyping.zarina.domain.geography.KladrId
import javax.inject.Inject

class GetDeliveryMethodsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val checkoutRepository: CheckoutRepository,
) : FlowUseCase<GetDeliveryMethodsFlowUseCase.Params, List<DeliveryMethod>>(dispatcher) {

    override fun execute(params: Params): Flow<List<DeliveryMethod>> {
        val cartType = params.cartType
        val cityKladrId = params.cityKladrId
        return checkoutRepository.getDeliveryMethodsFlow(cartType, cityKladrId)
    }

    data class Params(val cartType: CartType, val cityKladrId: KladrId)
}

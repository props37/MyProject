package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.geography.City
import javax.inject.Inject

class GetPickupStoresFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val checkoutRepository: CheckoutRepository,
) : FlowUseCase<GetPickupStoresFlowUseCase.Params, List<PickupStore>>(dispatcher) {

    override fun execute(params: Params): Flow<List<PickupStore>> {
        val city = params.city
        return checkoutRepository.getPickupStoresFlow(city.id)
    }

    data class Params(val city: City)
}

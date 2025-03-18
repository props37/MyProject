package ru.livetyping.zarina.usecase.checkout

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.checkout.CheckoutRepository
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.checkout.PickupStore
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import javax.inject.Inject

class GetPickupStoresFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val checkoutRepository: CheckoutRepository,
    private val userRepository: UserRepository,
) : FlowUseCase<GetPickupStoresFlowUseCase.Params, List<PickupStore>>(dispatcher) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<List<PickupStore>> {
        return userRepository.getUserCityFlow().flatMapLatest { city ->
            checkNotNull(city) { "city is null" }
            checkoutRepository.getPickupStoresFlow(city.id, params.deliveryMethodType)
                .map { stores ->
                    stores.sortedByDescending { it.availableItemCount }
                }
        }
    }

    data class Params(val deliveryMethodType: DeliveryMethodType)
}

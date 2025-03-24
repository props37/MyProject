package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPickupStoresFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetPickupStoresFlowUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<PickupStore>>(logger), GetPickupStoresFlowUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<List<PickupStore>> {
        return userRepository.getUserCityFlow(CachePolicy.LocalOnly).flatMapLatest { city ->
            checkNotNull(city) { "city is null" }
            checkoutRepository.getPickupStoresFlow(city.id, params.deliveryMethodType)
                .map { stores ->
                    stores.sortedByDescending { it.availableItemCount }
                }
        }
    }

    override fun invoke(params: Params): Flow<Result<List<PickupStore>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetPickupStoresFlowUseCaseImpl"
    }
}

package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.checkout.PickupPointShort
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPickupPointsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetPickupPointsFlowUseCaseImpl(
    private val userRepository: UserRepository,
    private val checkoutRepository: CheckoutRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<PickupPointShort>>(logger), GetPickupPointsFlowUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<List<PickupPointShort>> {
        return if (params.cityFiasId != null) {
            checkoutRepository.getPickupPointsFlow(params.cityFiasId)
        } else {
            userRepository.getUserCityFlow(CachePolicy.LocalOnly).flatMapLatest { city ->
                checkNotNull(city) { "city is null" }
                checkoutRepository.getPickupPointsFlow(city.id)
            }
        }
    }

    override fun invoke(params: Params): Flow<Result<List<PickupPointShort>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetPickupPointsFlowUseCaseImpl"
    }
}

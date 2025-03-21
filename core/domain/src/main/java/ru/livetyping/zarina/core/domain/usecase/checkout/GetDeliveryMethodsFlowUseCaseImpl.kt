package ru.livetyping.zarina.core.domain.usecase.checkout

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.domain.repository.CheckoutRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.checkout.GetDeliveryMethodsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetDeliveryMethodsFlowUseCaseImpl(
    private val checkoutRepository: CheckoutRepository,
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<DeliveryMethod>>(logger), GetDeliveryMethodsFlowUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<List<DeliveryMethod>> {
        return userRepository.getUserCityFlow(CachePolicy.LocalOnly).flatMapLatest { city ->
            checkNotNull(city) { "city is null" }
            checkoutRepository.getDeliveryMethodsFlow(params.cartType, city.id)
        }
    }

    override fun invoke(params: Params): Flow<Result<List<DeliveryMethod>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetDeliveryMethodsFlowUseCaseImpl"
    }
}

package ru.livetyping.zarina.usecase.product

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.product.ProductRepository
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.domain.product.ProductAvailabilityInStore
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.usecase.product.GetProductAvailabilityInStoresFlowUseCase.Params
import javax.inject.Inject

class GetProductAvailabilityInStoresFlowUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) : FlowUseCase<Params, List<ProductAvailabilityInStore>>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<List<ProductAvailabilityInStore>> {
        return userRepository.getUserCityFlow().flatMapLatest { city ->
            checkNotNull(city) { "city is null" }
            productRepository.getProductAvailabilityInStoresFlow(params.offer, city.id)
        }
    }

    data class Params(val offer: ProductOffer)
}

package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.product.GetProductAvailabilityInStoresFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetProductAvailabilityInStoresFlowUseCaseImpl(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<ProductAvailabilityInStore>>(logger),
    GetProductAvailabilityInStoresFlowUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<List<ProductAvailabilityInStore>> {
        return userRepository.getUserCityFlow(CachePolicy.LocalOnly).flatMapLatest { city ->
            checkNotNull(city) { "city is null" }
            productRepository.getProductAvailabilityInStoresFlow(params.offer, city.id)
        }
    }

    override fun invoke(params: Params): Flow<Result<List<ProductAvailabilityInStore>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetProductAvailabilityInStoresFlowUseCaseImpl"
    }
}

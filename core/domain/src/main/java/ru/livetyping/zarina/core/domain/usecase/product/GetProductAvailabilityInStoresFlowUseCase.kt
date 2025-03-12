package ru.livetyping.zarina.core.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.repository.ProductRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetProductAvailabilityInStoresFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<ProductAvailabilityInStore>>>

    public data class Params(val offer: ProductOffer)

    public companion object {
        public fun getInstance(
            productRepository: ProductRepository,
            userRepository: UserRepository,
            logger: UseCaseLogger?,
        ): GetProductAvailabilityInStoresFlowUseCase {
            return GetProductAvailabilityInStoresFlowUseCaseImpl(
                productRepository = productRepository,
                userRepository = userRepository,
                logger = logger,
            )
        }
    }
}

package ru.livetyping.zarina.usecase.old.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.product.IProductRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.DeliveryAvailability
import ru.livetyping.zarina.domain.old.Product
import timber.log.Timber

@Factory
class GetDeliveryAvailabilityUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
) : UseCase<GetDeliveryAvailabilityUseCase.Params, DeliveryAvailability>(dispatcher) {
    override suspend fun execute(params: Params): DeliveryAvailability {
        val (product) = params

        val availability = productRepository.getDeliveryAvailability(product)

        Timber.v("Delivery availability for product $product: $availability")

        return availability
    }

    data class Params(
        val product: Product,
    )
}

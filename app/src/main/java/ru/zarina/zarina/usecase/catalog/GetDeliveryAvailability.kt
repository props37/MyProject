package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.Product
import timber.log.Timber
import javax.inject.Inject

class GetDeliveryAvailabilityUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
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

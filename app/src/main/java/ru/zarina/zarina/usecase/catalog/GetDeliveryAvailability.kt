package ru.zarina.zarina.usecase.catalog

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.DeliveryAvailability
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.usecase.base.UseCase
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

package ru.zarina.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.product.IProductRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Product
import timber.log.Timber
import javax.inject.Inject

class GetOffersUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val productRepository: IProductRepository,
) : UseCase<GetOffersUseCase.Params, List<Offer>>(dispatcher) {
    override suspend fun execute(params: Params): List<Offer> {
        val (product, city) = params

        val offers = productRepository.getOffers(product, city)
        Timber.v("Got ${offers.size} offers for product ${product.id} in ${city.id}")
        return offers
    }

    data class Params(
        val product: Product,
        val city: City,
    )
}

package ru.zarina.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.product.IProductRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.base.usecase.UseCase
import timber.log.Timber

@Factory
class GetOffersUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
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

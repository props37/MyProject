package ru.livetyping.zarina.usecase.old.shop

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.product.IProductRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.Offer
import ru.livetyping.zarina.domain.old.Product
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

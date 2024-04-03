package ru.livetyping.zarina.usecase.old.shop

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.shop.IShopRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.Offer
import ru.livetyping.zarina.domain.old.Stock
import timber.log.Timber

@Factory
class GetStocksUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val shopRepository: IShopRepository,
) : UseCase<GetStocksUseCase.Params, List<Stock>>(dispatcher) {
    override suspend fun execute(params: Params): List<Stock> {
        val (offer, city) = params

        val stocks = shopRepository.getStocks(offer, city)

        Timber.v("Stocks for offer ${offer.id} in ${city.name}: $stocks")

        return stocks
    }

    data class Params(
        val offer: Offer,
        val city: City,
    )
}

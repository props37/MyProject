package ru.zarina.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.shop.IShopRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Stock
import timber.log.Timber
import javax.inject.Inject

class GetStocksUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
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

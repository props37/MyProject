package ru.zarina.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.shop.IShopRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.usecase.base.UseCase
import timber.log.Timber

@Factory
class GetShopsUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val shopRepository: IShopRepository,
) : UseCase<GetShopsUseCase.Params, List<Shop>>(dispatcher) {
    override suspend fun execute(params: Params): List<Shop> {
        val (city) = params

        val shops = shopRepository.getShops(city)

        Timber.v("Found ${shops.size} shops in ${city.name}")

        return shops
    }

    data class Params(
        val city: City,
    )
}

package ru.livetyping.zarina.usecase.old.shop

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.old.shop.IShopRepository
import ru.livetyping.zarina.di.old.Qualifiers
import ru.livetyping.zarina.domain.old.City
import ru.livetyping.zarina.domain.old.Shop
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

package ru.zarina.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.shop.IShopRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import timber.log.Timber
import javax.inject.Inject

/**
 * Returns a list of cities that have a Zarina shop supporting pickup.
 */
class GetPickupCitiesUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val shopRepository: IShopRepository,
) : UseCase<Unit, List<City>>(dispatcher) {
    override suspend fun execute(params: Unit): List<City> {
        val cities = shopRepository.getCountries()
            .filter { it.isPickupSupported }
            .flatMap { it.cities }
        Timber.v("Got ${cities.size} cities with Zarina shop supporting pickup")
        return cities
    }
}

package ru.zarina.zarina.usecase.old.shop

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.shop.IShopRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.base.usecase.UseCase
import timber.log.Timber

/**
 * Returns a list of cities that have a Zarina shop supporting pickup.
 */
@Factory
class GetPickupCitiesUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
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

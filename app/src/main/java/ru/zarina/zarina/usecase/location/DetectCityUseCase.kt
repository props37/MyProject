package ru.zarina.zarina.usecase.location

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.geography.GeographyRepository
import ru.zarina.zarina.data.location.IGeoLocationRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.City
import timber.log.Timber
import javax.inject.Inject

class DetectCityUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val geoLocationRepository: IGeoLocationRepository,
    private val geographyRepository: GeographyRepository,
) : UseCase<Unit, City?>(dispatcher) {
    override suspend fun execute(params: Unit): City? {
        val geoLocation = checkNotNull(geoLocationRepository.getCurrentLocation())
        // TODO if geoLocation is null, show "location services are not available"
        val city = geographyRepository.getCity(geoLocation)
        Timber.v("Current location: $geoLocation, detected city: $city")
        return city
    }
}

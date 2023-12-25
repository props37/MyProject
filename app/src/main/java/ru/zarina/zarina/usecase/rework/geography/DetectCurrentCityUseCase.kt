package ru.zarina.zarina.usecase.rework.geography

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withTimeout
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.rework.geography.GeographyRepository
import ru.zarina.zarina.data.rework.location.LocationRepository
import ru.zarina.zarina.di.reworked.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class DetectCurrentCityUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
    private val geographyRepository: GeographyRepository,
) : UseCase<Unit, City?>(dispatcher) {

    override suspend fun execute(params: Unit): City? {
        val location = withTimeout(5.seconds) {
            locationRepository.getCurrentLocation()
        }
        Timber.v("Current location: $location")
        return if (location != null) {
            val city = geographyRepository.getCity(location)
            Timber.v("The city is detected: $city")
            return city
        } else {
            Timber.v("Could not detect the city because the location is unknown")
            null
        }
    }
}

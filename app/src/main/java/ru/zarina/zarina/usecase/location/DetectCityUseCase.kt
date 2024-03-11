package ru.zarina.zarina.usecase.location

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.geography.IGeographyRepository
import ru.zarina.zarina.data.location.IGeoLocationRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.exception.ServiceUnavailableException
import ru.zarina.zarina.base.usecase.UseCase
import timber.log.Timber

@Factory
class DetectCityUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val geoLocationRepository: IGeoLocationRepository,
    private val geographyRepository: IGeographyRepository,
) : UseCase<Unit, City?>(dispatcher) {
    override suspend fun execute(params: Unit): City? {
        val geoLocation = geoLocationRepository.getCurrentLocation()
            ?: throw ServiceUnavailableException("Location service is unavailable")
        val city = geographyRepository.getCity(geoLocation)
        Timber.v("Current location: $geoLocation, detected city: $city")
        return city
    }
}

package ru.zarina.zarina.usecase.rework.geography

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zarina.zarina.data.rework.geography.GeographyRepository
import ru.zarina.zarina.data.rework.location.LocationRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.base.usecase.FlowUseCase
import timber.log.Timber
import javax.inject.Inject

class GetCurrentCityFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
    private val geographyRepository: GeographyRepository,
) : FlowUseCase<Unit, City?>(dispatcher) {

    override fun execute(params: Unit): Flow<City?> {
        return locationRepository.getCurrentLocationFlow().map { location ->
            if (location != null) {
                val city = geographyRepository.getCity(location)
                Timber.v("The city is detected: $city")
                city
            } else {
                Timber.v("Could not detect a city because the location is unknown")
                null
            }
        }
    }
}

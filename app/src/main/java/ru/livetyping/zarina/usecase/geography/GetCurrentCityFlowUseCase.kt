package ru.livetyping.zarina.usecase.geography

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.geography.GeographyRepository
import ru.livetyping.zarina.data.location.LocationRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.geography.City
import timber.log.Timber
import javax.inject.Inject

class GetCurrentCityFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
    private val geographyRepository: GeographyRepository,
) : FlowUseCase<Unit, City?>(dispatcher) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<City?> {
        return locationRepository.getCurrentLocationFlow().flatMapLatest { location ->
            if (location != null) {
                geographyRepository.getCityFlow(location)
            } else {
                Timber.v("Could not detect a city because the location is unknown")
                flowOf(null)
            }
        }
    }
}

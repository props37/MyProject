package ru.livetyping.zarina.core.domain.usecase.geo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.domain.repository.LocationRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCurrentCityByLocationFlowUseCaseImpl(
    private val locationRepository: LocationRepository,
    private val geographyRepository: GeographyRepository,
    private val logger: UseCaseLogger?,
) : FlowUseCase<Unit, City?>(logger), GetCurrentCityByLocationFlowUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<City?> {
        return locationRepository.getCurrentLocationFlow().flatMapLatest { location ->
            if (location != null) {
                geographyRepository.getCityByLocationFlow(location)
            } else {
                logger?.v(TAG, "Could not get a city by location because the location is unknown")
                flowOf(null)
            }
        }
    }

    override fun invoke(): Flow<Result<City?>> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "GetCurrentCityByLocationFlowUseCaseImpl"
    }
}

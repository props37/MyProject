package ru.livetyping.zarina.core.domain.usecase.geo

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.domain.repository.LocationRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCurrentCityByLocationFlowUseCase {
    public operator fun invoke(): Flow<Result<City?>>

    public companion object {
        public fun getInstance(
            locationRepository: LocationRepository,
            geographyRepository: GeographyRepository,
            logger: UseCaseLogger?,
        ): GetCurrentCityByLocationFlowUseCase {
            return GetCurrentCityByLocationFlowUseCaseImpl(
                locationRepository = locationRepository,
                geographyRepository = geographyRepository,
                logger = logger,
            )
        }
    }
}

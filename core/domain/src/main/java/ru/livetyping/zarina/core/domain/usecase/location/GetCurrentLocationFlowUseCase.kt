package ru.livetyping.zarina.core.domain.usecase.location

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.repository.LocationRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCurrentLocationFlowUseCase {
    public operator fun invoke(): Flow<Result<Location?>>

    public companion object {
        public fun getInstance(
            locationRepository: LocationRepository,
            logger: UseCaseLogger?,
        ): GetCurrentLocationFlowUseCase {
            return GetCurrentLocationFlowUseCaseImpl(
                locationRepository = locationRepository,
                logger = logger,
            )
        }
    }
}

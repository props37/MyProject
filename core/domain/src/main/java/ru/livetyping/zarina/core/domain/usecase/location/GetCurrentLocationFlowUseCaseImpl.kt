package ru.livetyping.zarina.core.domain.usecase.location

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.repository.LocationRepository
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCurrentLocationFlowUseCaseImpl(
    private val locationRepository: LocationRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, Location?>(logger), GetCurrentLocationFlowUseCase {

    override fun execute(params: Unit): Flow<Location?> {
        return locationRepository.getCurrentLocationFlow()
    }

    override fun invoke(): Flow<Result<Location?>> {
        return call(Unit)
    }
}

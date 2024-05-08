package ru.livetyping.zarina.usecase.location

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.location.LocationRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.location.Location
import javax.inject.Inject

class GetCurrentLocationFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
) : FlowUseCase<Unit, Location?>(dispatcher) {

    override fun execute(params: Unit): Flow<Location?> {
        return locationRepository.getCurrentLocationFlow()
    }
}

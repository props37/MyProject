package ru.livetyping.zarina.usecase.device

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.device.DeviceRepository
import ru.livetyping.zarina.di.Qualifiers
import javax.inject.Inject

class GetIsOnboardingCompletedFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val deviceRepository: DeviceRepository,
) : FlowUseCase<Unit, Boolean>(dispatcher) {

    override fun execute(params: Unit): Flow<Boolean> {
        return deviceRepository.getIsOnboardingCompletedFlow()
    }
}

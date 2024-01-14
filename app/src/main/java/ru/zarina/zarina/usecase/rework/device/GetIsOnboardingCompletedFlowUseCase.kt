package ru.zarina.zarina.usecase.rework.device

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.device.DeviceRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.usecase.base.FlowUseCase
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

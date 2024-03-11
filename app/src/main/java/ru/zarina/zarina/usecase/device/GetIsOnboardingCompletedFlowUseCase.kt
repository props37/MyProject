package ru.zarina.zarina.usecase.device

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.device.DeviceRepository
import ru.zarina.zarina.di.Qualifiers
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

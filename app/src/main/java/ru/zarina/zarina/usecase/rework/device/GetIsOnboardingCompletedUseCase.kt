package ru.zarina.zarina.usecase.rework.device

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.clean.FlowUseCase
import ru.zarina.zarina.data.device.DeviceRepository
import ru.zarina.zarina.di.reworked.Qualifiers
import javax.inject.Inject

class GetIsOnboardingCompletedUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val deviceRepository: DeviceRepository,
) : FlowUseCase<Unit, Boolean>(dispatcher) {

    override fun execute(params: Unit): Flow<Boolean> {
        return deviceRepository.getIsOnboardingCompleted()
    }
}

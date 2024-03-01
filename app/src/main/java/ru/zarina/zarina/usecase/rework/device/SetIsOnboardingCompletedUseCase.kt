package ru.zarina.zarina.usecase.rework.device

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.device.DeviceRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.usecase.base.UseCase
import javax.inject.Inject

class SetIsOnboardingCompletedUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val deviceRepository: DeviceRepository,
) : UseCase<SetIsOnboardingCompletedUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val isCompleted = params.isCompleted
        deviceRepository.setIsOnboardingCompleted(isCompleted)
    }

    data class Params(val isCompleted: Boolean)
}

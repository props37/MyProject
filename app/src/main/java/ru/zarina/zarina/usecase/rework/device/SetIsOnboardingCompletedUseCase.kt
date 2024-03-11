package ru.zarina.zarina.usecase.rework.device

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.device.DeviceRepository
import ru.zarina.zarina.di.Qualifiers
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

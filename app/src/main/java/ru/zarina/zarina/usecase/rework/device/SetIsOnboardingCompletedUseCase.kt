package ru.zarina.zarina.usecase.rework.device

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.rework.device.DeviceRepository
import ru.zarina.zarina.di.reworked.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class SetIsOnboardingCompletedUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val deviceRepository: DeviceRepository,
) : UseCase<SetIsOnboardingCompletedUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val isCompleted = params.isCompleted
        Timber.v("Set is onboarding completed: $isCompleted")
        deviceRepository.setIsOnboardingCompleted(isCompleted)
    }

    data class Params(val isCompleted: Boolean)
}

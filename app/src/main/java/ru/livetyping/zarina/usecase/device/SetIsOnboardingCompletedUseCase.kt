package ru.livetyping.zarina.usecase.device

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.device.DeviceRepository
import ru.livetyping.zarina.di.Qualifiers
import timber.log.Timber
import javax.inject.Inject

class SetIsOnboardingCompletedUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val deviceRepository: DeviceRepository,
) : UseCase<SetIsOnboardingCompletedUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val isCompleted = params.isCompleted
        Timber.v("Set onboarding completed: $isCompleted")
        deviceRepository.setIsOnboardingCompleted(isCompleted)
    }

    data class Params(val isCompleted: Boolean)
}

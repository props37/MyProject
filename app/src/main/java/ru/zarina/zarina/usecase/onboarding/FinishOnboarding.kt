package ru.zarina.zarina.usecase.onboarding

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.device.IDeviceRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import javax.inject.Inject

class FinishOnboardingUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val deviceRepository: IDeviceRepository,
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun execute(params: Unit) {
        deviceRepository.setIsOnboardingCompleted(true)
    }
}

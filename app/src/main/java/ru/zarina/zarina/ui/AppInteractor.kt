package ru.zarina.zarina.ui

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.device.IDeviceRepository

@Factory
class AppInteractor(
    private val deviceRepository: IDeviceRepository,
) {
    fun isOnboardingCompleted() = deviceRepository.getIsOnboardingCompleted()
}

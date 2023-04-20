package ru.zarina.zarina.ui

import ru.zarina.zarina.data.device.IDeviceRepository
import javax.inject.Inject

class AppInteractor @Inject constructor(
    private val deviceRepository: IDeviceRepository,
) {
    fun isOnboardingCompleted() = deviceRepository.getIsOnboardingCompleted()
}

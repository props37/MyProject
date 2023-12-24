package ru.zarina.zarina.data.rework.device.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeviceLocalDataSource @Inject constructor(
    private val onboardingDataHolder: OnboardingDataHolder,
) {
    fun getIsOnboardingCompleted(): Flow<Boolean> {
        return onboardingDataHolder.getIsOnboardingCompleted()
    }

    suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        onboardingDataHolder.setIsOnboardingCompleted(isCompleted)
    }
}

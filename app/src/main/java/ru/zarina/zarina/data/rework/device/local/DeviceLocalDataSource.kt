package ru.zarina.zarina.data.rework.device.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeviceLocalDataSource @Inject constructor(
    private val onboardingDataHolder: OnboardingDataHolder,
) {
    fun getIsOnboardingCompletedFlow(): Flow<Boolean> {
        return onboardingDataHolder.getIsOnboardingCompletedFlow()
    }

    suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        onboardingDataHolder.setIsOnboardingCompleted(isCompleted)
    }
}

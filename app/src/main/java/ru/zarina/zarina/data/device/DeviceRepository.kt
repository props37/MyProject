package ru.zarina.zarina.data.device

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.device.local.DeviceLocalDataSource
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val localDataSource: DeviceLocalDataSource,
) {
    fun getIsOnboardingCompletedFlow(): Flow<Boolean> {
        return localDataSource.getIsOnboardingCompletedFlow()
    }

    suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        localDataSource.setIsOnboardingCompleted(isCompleted)
    }
}

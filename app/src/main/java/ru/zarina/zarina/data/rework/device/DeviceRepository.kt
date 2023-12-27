package ru.zarina.zarina.data.rework.device

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.device.local.DeviceLocalDataSource
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val localDataSource: DeviceLocalDataSource,
) {
    fun getIsOnboardingCompleted(): Flow<Boolean> {
        return localDataSource.getIsOnboardingCompleted()
    }

    suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        localDataSource.setIsOnboardingCompleted(isCompleted)
    }
}

package ru.livetyping.zarina.data.old.device

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.AuthorizationToken

interface IDeviceRepository {
    suspend fun getToken(): AuthorizationToken.Device
    suspend fun setToken(token: AuthorizationToken.Device?)
    fun getIsOnboardingCompleted(): Flow<Boolean>
    suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean)
}

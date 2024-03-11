package ru.zarina.zarina.data.old.device.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.old.AuthorizationToken

interface IDeviceLocalSource {

    fun getToken(): Flow<AuthorizationToken.Device?>

    suspend fun setToken(token: AuthorizationToken.Device?)

    fun getIsOnboardingCompleted(): Flow<Boolean>

    suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean)

}

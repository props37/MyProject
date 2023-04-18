package ru.zarina.zarina.data.device.local

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.AuthorizationToken

interface IDeviceLocalSource {

    fun getToken(): Flow<AuthorizationToken.Device?>

    suspend fun setToken(token: AuthorizationToken.Device?)

}

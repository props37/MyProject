package ru.zarina.zarina.data.device

import ru.zarina.zarina.domain.AuthorizationToken

interface IDeviceRepository {
    suspend fun getToken(): AuthorizationToken.Device
    suspend fun setToken(token: AuthorizationToken.Device?)
}

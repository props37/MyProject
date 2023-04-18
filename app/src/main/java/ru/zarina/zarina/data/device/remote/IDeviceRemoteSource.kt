package ru.zarina.zarina.data.device.remote

import ru.zarina.zarina.domain.AuthorizationToken

interface IDeviceRemoteSource {
    suspend fun getToken(): AuthorizationToken.Device
}

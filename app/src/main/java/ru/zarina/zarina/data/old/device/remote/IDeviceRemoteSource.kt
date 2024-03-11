package ru.zarina.zarina.data.old.device.remote

import ru.zarina.zarina.domain.AuthorizationToken

interface IDeviceRemoteSource {
    suspend fun getToken(): AuthorizationToken.Device
}

package ru.livetyping.zarina.data.old.device.remote

import ru.livetyping.zarina.domain.old.AuthorizationToken

interface IDeviceRemoteSource {
    suspend fun getToken(): AuthorizationToken.Device
}

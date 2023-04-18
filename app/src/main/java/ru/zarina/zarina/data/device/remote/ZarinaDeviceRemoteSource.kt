package ru.zarina.zarina.data.device.remote

import ru.zarina.zarina.data.device.remote.api.IZarinaDeviceApi
import ru.zarina.zarina.domain.AuthorizationToken
import javax.inject.Inject

class ZarinaDeviceRemoteSource @Inject constructor(
    private val api: IZarinaDeviceApi,
) : IDeviceRemoteSource {

    override suspend fun getToken(): AuthorizationToken.Device {
        return api.getToken().toDomain()
    }

}

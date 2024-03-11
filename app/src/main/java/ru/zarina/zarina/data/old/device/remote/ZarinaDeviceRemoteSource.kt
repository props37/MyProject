package ru.zarina.zarina.data.old.device.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.device.remote.api.IZarinaDeviceApi
import ru.zarina.zarina.domain.AuthorizationToken

@Factory
class ZarinaDeviceRemoteSource(
    private val api: IZarinaDeviceApi,
) : IDeviceRemoteSource {

    override suspend fun getToken(): AuthorizationToken.Device {
        return api.getToken().toDomain()
    }

}

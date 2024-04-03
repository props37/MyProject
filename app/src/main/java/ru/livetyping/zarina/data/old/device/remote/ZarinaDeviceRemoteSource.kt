package ru.livetyping.zarina.data.old.device.remote

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.device.remote.api.IZarinaDeviceApi
import ru.livetyping.zarina.domain.old.AuthorizationToken

@Factory
class ZarinaDeviceRemoteSource(
    private val api: IZarinaDeviceApi,
) : IDeviceRemoteSource {

    override suspend fun getToken(): AuthorizationToken.Device {
        return api.getToken().toDomain()
    }

}

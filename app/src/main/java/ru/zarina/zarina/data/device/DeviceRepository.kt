package ru.zarina.zarina.data.device

import ru.zarina.zarina.data.device.local.IDeviceLocalSource
import ru.zarina.zarina.data.device.remote.IDeviceRemoteSource
import ru.zarina.zarina.domain.AuthorizationToken
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val local: IDeviceLocalSource,
    private val remote: IDeviceRemoteSource,
) : IDeviceRepository {

    override suspend fun getToken(): AuthorizationToken.Device = remote.getToken()

}

package ru.zarina.zarina.data.device.remote

import ru.zarina.zarina.data.device.remote.api.IDeviceApi
import javax.inject.Inject

class DeviceRemoteSource @Inject constructor(
    private val api: IDeviceApi,
) : IDeviceRemoteSource

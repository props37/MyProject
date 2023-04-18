package ru.zarina.zarina.data.device.remote

import ru.zarina.zarina.data.device.remote.api.IZarinaDeviceApi
import javax.inject.Inject

class ZarinaDeviceRemoteSource @Inject constructor(
    private val api: IZarinaDeviceApi,
) : IDeviceRemoteSource

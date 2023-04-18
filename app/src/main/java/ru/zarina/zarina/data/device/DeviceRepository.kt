package ru.zarina.zarina.data.device

import kotlinx.coroutines.flow.first
import ru.zarina.zarina.data.device.local.IDeviceLocalSource
import ru.zarina.zarina.data.device.remote.IDeviceRemoteSource
import ru.zarina.zarina.domain.AuthorizationToken
import timber.log.Timber
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    private val local: IDeviceLocalSource,
    private val remote: IDeviceRemoteSource,
) : IDeviceRepository {

    override suspend fun getToken(): AuthorizationToken.Device {
        val localToken = local.getToken().first()
        if (localToken != null) return localToken
        Timber.v("No local device token is present, getting new one")
        val remoteToken = remote.getToken()
        local.setToken(remoteToken)
        return remoteToken
    }

    override suspend fun setToken(token: AuthorizationToken.Device?) = local.setToken(token)

    override fun getIsOnboardingCompleted() = local.getIsOnboardingCompleted()

    override suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean) =
        local.setIsOnboardingCompleted(isOnboardingCompleted)

}

package ru.zarina.zarina.data.old.device

import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.device.local.IDeviceLocalSource
import ru.zarina.zarina.data.old.device.remote.IDeviceRemoteSource
import ru.zarina.zarina.domain.old.AuthorizationToken
import timber.log.Timber

@Factory
class DeviceRepository(
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

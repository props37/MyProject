package ru.livetyping.zarina.data.mindbox.impl.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import ru.livetyping.zarina.core.buildutil.MindboxDeviceUuidProvider
import ru.livetyping.zarina.core.buildutil.MindboxEndpoint
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.network.util.setJsonBody
import ru.livetyping.zarina.data.mindbox.impl.api.dto.UserDto
import javax.inject.Inject
import ru.livetyping.zarina.core.network.di.MindboxApi as MindboxApiQualifier

internal class MindboxApiImpl @Inject constructor(
    @MindboxApiQualifier
    private val httpClient: HttpClient,
    private val mindboxDeviceUuidProvider: MindboxDeviceUuidProvider,
    @MindboxEndpoint
    private val mindboxEndpoint: String,
) : MindboxApi {
    override suspend fun onUserSignedUp(user: User) {
        val deviceUuid = mindboxDeviceUuidProvider.getDeviceUuid()
        if (deviceUuid != null) {
            httpClient.post("v3/operations/async?endpointId=$mindboxEndpoint&operation=signupmobile&deviceUUID=$deviceUuid") {
                val body = UserDto.from(user)
                setJsonBody(body)
            }
        }
    }

    override suspend fun onUserSignedIn(user: User) {
        val deviceUuid = mindboxDeviceUuidProvider.getDeviceUuid()
        if (deviceUuid != null) {
            httpClient.post("v3/operations/async?endpointId=$mindboxEndpoint&operation=customerloginMobileApp&deviceUUID=$deviceUuid") {
                val body = UserDto.from(user)
                setJsonBody(body)
            }
        }
    }
}

package ru.livetyping.zarina.data.mindbox

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.core.buildutil.MindboxDeviceUuidProvider
import ru.livetyping.zarina.data.mindbox.dto.UserAuthorizedDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.user.User
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class MindboxApi @Inject constructor(
    @Qualifiers.MindboxApi
    private val httpClient: HttpClient,
    private val mindboxDeviceUuidProvider: MindboxDeviceUuidProvider,
) {
    suspend fun userSignedUp(user: User) {
        val deviceUuid = mindboxDeviceUuidProvider.getDeviceUuid()
        if (deviceUuid != null) {
            httpClient.post("v3/operations/async?endpointId=${BuildConfig.MINDBOX_ENDPOINT}&operation=signupmobile&deviceUUID=$deviceUuid") {
                val body = UserAuthorizedDto.from(user)
                setJsonBody(body)
            }
        }
    }

    suspend fun userSignedIn(user: User) {
        val deviceUuid = mindboxDeviceUuidProvider.getDeviceUuid()
        if (deviceUuid != null) {
            httpClient.post("v3/operations/async?endpointId=${BuildConfig.MINDBOX_ENDPOINT}&operation=customerloginMobileApp&deviceUUID=$deviceUuid") {
                val body = UserAuthorizedDto.from(user)
                setJsonBody(body)
            }
        }
    }
}

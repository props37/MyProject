package ru.livetyping.zarina.data.mindbox

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.data.mindbox.dto.UserAuthorizedDto
import ru.livetyping.zarina.di.MindboxDeviceUuidProvider
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
        val uuid = getDeviceUuid()
        httpClient.post("v3/operations/async?endpointId=${BuildConfig.MINDBOX_ENDPOINT}&operation=signupmobile&deviceUUID=$uuid") {
            val body = UserAuthorizedDto.from(user)
            setJsonBody(body)
        }
    }

    suspend fun userSignedIn(user: User) {
        val uuid = getDeviceUuid()
        httpClient.post("v3/operations/async?endpointId=${BuildConfig.MINDBOX_ENDPOINT}&operation=customerloginMobileApp&deviceUUID=$uuid") {
            val body = UserAuthorizedDto.from(user)
            setJsonBody(body)
        }
    }

    private suspend fun getDeviceUuid(): String {
        val uuid = mindboxDeviceUuidProvider.getMindboxDeviceUuidFlow().firstOrNull()
        return checkNotNull(uuid) { "Mindbox device UUID is null" }
    }
}

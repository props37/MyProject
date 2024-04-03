package ru.livetyping.zarina.application.extension

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.usecase.authorization.FetchUnauthorizedUserAuthorizationTokensUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import javax.inject.Inject

class AuthorizationTokenFetcherApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val fetchUnauthorizedUserAuthorizationTokens: FetchUnauthorizedUserAuthorizationTokensUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        coroutineScope.launch {
            fetchUnauthorizedUserAuthorizationTokens()
        }
    }
}
